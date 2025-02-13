package ru.crazerr.avitotest.data.remote

import ru.crazerr.avitotest.data.model.toPreviewTrack
import ru.crazerr.avitotest.data.model.toTracks
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.domain.model.Track
import javax.inject.Inject

internal class TrackRemoteDataSource @Inject constructor(
    private val deezerService: DeezerService,
) {
    suspend fun getChart(page: Int, pageSize: Int): Result<List<PreviewTrack>> {
        return try {
            val tracks = deezerService.getChart(offset = (page - 1) * pageSize, limit = pageSize)

            Result.success(tracks.tracks.data.map { it.toPreviewTrack() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getSearch(
        searchQuery: String,
        page: Int,
        pageSize: Int
    ): Result<List<PreviewTrack>> {
        return try {
            val response = deezerService.getSearch(
                query = searchQuery,
                offset = (page - 1) * pageSize,
                limit = pageSize
            )

            Result.success(response.data.map { it.toPreviewTrack() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getTrackById(id: Long): Result<Track> {
        return try {
            val track = deezerService.getTrackById(id = id)

            Result.success(track.toTracks())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getTrackQueueByChart(position: Int): Result<List<Long>> {
        return try {
            val initialPageSize = 30
            val offset = position / initialPageSize

            val tracks =
                deezerService.getTrackQueueByChart(
                    offset = offset,
                    limit = initialPageSize
                ).tracks.data

            val index = tracks.indexOfFirst { it.position == position }

            if (index == -1) {
                Result.success(tracks.map { it.id })
            } else {
                Result.success(tracks.slice(index + 1..tracks.lastIndex).map { it.id })
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}