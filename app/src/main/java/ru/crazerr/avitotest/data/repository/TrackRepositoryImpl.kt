package ru.crazerr.avitotest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.avitotest.data.local.TrackLocalDataSource
import ru.crazerr.avitotest.data.paging.LocalTrackPagingSource
import ru.crazerr.avitotest.domain.model.Track
import ru.crazerr.avitotest.domain.repository.TrackRepository
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackLocalDataSource: TrackLocalDataSource,
) : TrackRepository {
    override fun getLocalTracks(searchQuery: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(pageSize = 30, initialLoadSize = 30),
            pagingSourceFactory = {
                LocalTrackPagingSource(
                    trackLocalDataSource = trackLocalDataSource,
                    searchQuery = searchQuery.ifBlank { null })
            }
        ).flow
    }
}