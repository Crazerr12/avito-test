package ru.crazerr.avitotest.feature.baseTracks

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.crazerr.avitotest.domain.model.Track

data class BaseTracksState(
    val tracks: Flow<PagingData<Track>>,
    val searchQuery: String,
)

internal val InitialBaseTracksState = BaseTracksState(
    tracks = emptyFlow(),
    searchQuery = "",
)
