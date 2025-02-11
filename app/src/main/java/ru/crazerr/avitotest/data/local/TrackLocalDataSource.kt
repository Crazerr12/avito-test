package ru.crazerr.avitotest.data.local

import android.content.Context
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.crazerr.avitotest.domain.model.Track
import javax.inject.Inject

class TrackLocalDataSource @Inject constructor(private val context: Context) {
    suspend fun getLocalTracks(
        page: Int,
        pageSize: Int,
        searchQuery: String?,
    ): Result<List<Track>> =
        withContext(Dispatchers.IO) {
            try {
                val tracks = mutableListOf<Track>()

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

                            tracks.add(
                                Track(
                                    id = id,
                                    image = "content://media/external/audio/albumart/$albumId",
                                    title = title,
                                    author = artist
                                )
                            )
                        } while (cursor.moveToNext() && tracks.size < pageSize)
                    }
                }
                Result.success(tracks)
            } catch (ex: Exception) {
                Result.failure(ex)
            }
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