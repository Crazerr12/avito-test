package ru.crazerr.avitotest.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.crazerr.avitotest.domain.model.Track

interface TrackRepository {
    fun getLocalTracks(searchQuery: String): Flow<PagingData<Track>>
}