package ru.crazerr.avitotest.data.paging

import ru.crazerr.avitotest.data.local.TrackLocalDataSource
import ru.crazerr.avitotest.domain.model.Track
import ru.crazerr.avitotest.utils.paging.BasePagingSource

class LocalTrackPagingSource(
    private val trackLocalDataSource: TrackLocalDataSource,
    private val searchQuery: String?,
) : BasePagingSource<Track>() {
    override suspend fun loadPage(
        page: Int,
        pageSize: Int,
    ): Result<List<Track>> {
        return trackLocalDataSource.getLocalTracks(
            page = page,
            pageSize = pageSize,
            searchQuery = searchQuery
        )
    }
}