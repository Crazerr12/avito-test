package ru.crazerr.avitotest.data.local

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.domain.model.Track
import javax.inject.Inject

internal class TrackLocalDataSource @Inject constructor(private val context: Context) {
    suspend fun getLocalTracks(
        page: Int,
        pageSize: Int,
        searchQuery: String?,
    ): Result<List<PreviewTrack>> =
        withContext(Dispatchers.IO) {
            try {
                val previewTracks = mutableListOf<PreviewTrack>()

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM_ID,
                )
                val (selection, selectionArgs) = buildSearchConditions(searchQuery)
                val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

                context.contentResolver.query(
                    uri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder
                )?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                    val totalCount = cursor.count
                    val offset = (page - 1) * pageSize

                    if (offset >= totalCount) return@use

                    if (cursor.moveToPosition(offset)) {
                        do {
                            val id = cursor.getLong(idColumn)
                            val title = cursor.getString(titleColumn)
                            val artist = cursor.getString(artistColumn)
                            val albumId = cursor.getLong(albumColumn)

                            previewTracks.add(
                                PreviewTrack(
                                    id = id,
                                    image = "content://media/external/audio/albumart/$albumId",
                                    title = title,
                                    author = artist,
                                )
                            )
                        } while (cursor.moveToNext() && previewTracks.size < pageSize)
                    }
                }
                Result.success(previewTracks)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
        }

    suspend fun getLocalTrackById(id: Long): Result<Track> = withContext(Dispatchers.IO) {
        try {
            val uri =
                ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

            context.contentResolver.query(
                uri,
                projection,
                selection,
                null,
                MediaStore.Audio.Media._ID + " ASC"
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    return@withContext Result.success(cursor.toTrack())
                }
            }

            val allTracksUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            context.contentResolver.query(
                allTracksUri,
                projection,
                selection,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    return@withContext Result.success(cursor.toTrack())
                }
            }

            Result.failure(Exception())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    private fun Cursor.toTrack(): Track {
        val idColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
        val pathColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val titleColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val albumIdColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val albumColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
        val durationColumn = this.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

        val id = this.getLong(idColumn)
        val path = this.getString(pathColumn)
        val title = this.getString(titleColumn)
        val artist = this.getString(artistColumn)
        val albumId = this.getLong(albumIdColumn)
        val album = this.getString(albumColumn)
        val duration = this.getLong(durationColumn)

        return Track(
            id = id,
            trackImage = null,
            albumImage = "content://media/external/audio/albumart/$albumId",
            title = title,
            author = artist,
            albumId = albumId,
            album = album,
            url = path,
            duration = duration,
        )
    }

    private fun buildSearchConditions(query: String?): Pair<String, Array<String>?> {
        val baseCondition = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        return if (!query.isNullOrBlank()) {
            val searchCondition = "${MediaStore.Audio.Media.TITLE} LIKE ?"
            val fullCondition = "$baseCondition AND $searchCondition"
            val searchArg = "%${query.trim()}%"
            Pair(fullCondition, arrayOf(searchArg))
        } else {
            Pair(baseCondition, null)
        }
    }
}