package ru.crazerr.avitotest.presentation.apiTracks

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.crazerr.avitotest.domain.model.Track
import ru.crazerr.avitotest.domain.repository.TrackRepository
import ru.crazerr.avitotest.feature.baseTracks.BaseTracksViewModel
import javax.inject.Inject

@HiltViewModel
class ApiTracksViewModel @Inject constructor(
    private val trackRepository: TrackRepository
) : BaseTracksViewModel() {

    init {
        getTracks()
    }

    override fun getTracks() {
        val tracks = trackRepository.getApiTracks(searchQuery = state.value.searchQuery)
            .cachedIn(viewModelScope)

        reduceState { copy(tracks = tracks) }
    }

    override fun onClickTrack(track: Track) {
        TODO("Not yet implemented")
    }

    override fun onUpdateSearchQuery(search: String) {
        reduceState { copy(searchQuery = search) }

        searchQueryThrottledLambda()
    }

    override fun onClearSearchQuery() {
        reduceState { copy(searchQuery = "") }

        searchQueryThrottledLambda()
    }
}