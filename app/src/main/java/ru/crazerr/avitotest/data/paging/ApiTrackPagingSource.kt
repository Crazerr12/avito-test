package ru.crazerr.avitotest.data.paging

import ru.crazerr.avitotest.data.remote.TrackRemoteDataSource
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.utils.paging.BasePagingSource

internal class ApiTrackPagingSource(
    private val trackRemoteDataSource: TrackRemoteDataSource,
    private val searchQuery: String,
) : BasePagingSource<PreviewTrack>() {
    override suspend fun loadPage(
        page: Int,
        pageSize: Int
    ): Result<List<PreviewTrack>> {
        return if (searchQuery.isBlank()) {
            trackRemoteDataSource.getChart(page = page, pageSize = pageSize)
        } else {
            trackRemoteDataSource.getSearch(
                searchQuery = searchQuery,
                page = page,
                pageSize = pageSize
            )
        }
    }
}