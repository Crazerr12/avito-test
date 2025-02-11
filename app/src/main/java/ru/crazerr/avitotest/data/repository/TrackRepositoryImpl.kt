package ru.crazerr.avitotest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.avitotest.data.local.TrackLocalDataSource
import ru.crazerr.avitotest.data.paging.ApiTrackPagingSource
import ru.crazerr.avitotest.data.paging.LocalTrackPagingSource
import ru.crazerr.avitotest.data.remote.TrackRemoteDataSource
import ru.crazerr.avitotest.domain.model.Track
import ru.crazerr.avitotest.domain.repository.TrackRepository
import javax.inject.Inject

private const val DEFAULT_PAGE_SIZE = 30

internal class TrackRepositoryImpl @Inject constructor(
    private val trackLocalDataSource: TrackLocalDataSource,
    private val trackRemoteDataSource: TrackRemoteDataSource,
) : TrackRepository {
    override fun getLocalTracks(searchQuery: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                LocalTrackPagingSource(
                    trackLocalDataSource = trackLocalDataSource,
                    searchQuery = searchQuery.ifBlank { null })
            }
        ).flow
    }

    override fun getApiTracks(searchQuery: String): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                ApiTrackPagingSource(
                    trackRemoteDataSource = trackRemoteDataSource,
                    searchQuery = searchQuery
                )
            }
        ).flow
    }
}