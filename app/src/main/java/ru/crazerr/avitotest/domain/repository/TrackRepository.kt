package ru.crazerr.avitotest.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.domain.model.Track

interface TrackRepository {
    fun getLocalTracks(searchQuery: String): Flow<PagingData<PreviewTrack>>

    fun getApiTracks(searchQuery: String): Flow<PagingData<PreviewTrack>>

    suspend fun getLocalTrackById(id: Long): Result<Track>

    suspend fun getApiTrackById(id: Long): Result<Track>

    suspend fun getTrackQueueByChart(position: Int): Result<List<Long>>
}