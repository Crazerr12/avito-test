package ru.crazerr.avitotest.data.remote

import ru.crazerr.avitotest.data.model.toTrack
import ru.crazerr.avitotest.data.model.toTracks
import ru.crazerr.avitotest.domain.model.Track
import javax.inject.Inject

internal class TrackRemoteDataSource @Inject constructor(
    private val deezerService: DeezerService,
) {
    suspend fun getChart(page: Int, pageSize: Int): Result<List<Track>> {
        return try {
            val tracks = deezerService.getChart(offset = (page - 1) * pageSize, limit = pageSize)

            Result.success(tracks.toTracks())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getSearch(searchQuery: String, page: Int, pageSize: Int): Result<List<Track>> {
        return try {
            val tracks = deezerService.getSearch(
                query = searchQuery,
                offset = (page - 1) * pageSize,
                limit = pageSize
            )

            Result.success(tracks.data.map { it.toTrack() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}