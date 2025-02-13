package ru.crazerr.avitotest.data.paging

import ru.crazerr.avitotest.data.local.TrackLocalDataSource
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.utils.paging.BasePagingSource

internal class LocalTrackPagingSource(
    private val trackLocalDataSource: TrackLocalDataSource,
    private val searchQuery: String?,
) : BasePagingSource<PreviewTrack>() {
    override suspend fun loadPage(
        page: Int,
        pageSize: Int,
    ): Result<List<PreviewTrack>> {
        return trackLocalDataSource.getLocalTracks(
            page = page,
            pageSize = pageSize,
            searchQuery = searchQuery
        )
    }
}