package ru.crazerr.avitotest.presentation.apiTracks

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.domain.repository.TrackRepository
import ru.crazerr.avitotest.feature.baseTracks.BaseTracksViewModel

@HiltViewModel(assistedFactory = ApiTracksViewModel.ApiTrackViewModelFactory::class)
class ApiTracksViewModel @AssistedInject constructor(
    private val trackRepository: TrackRepository,
    @Assisted private val onAction: (ApiTracksViewModelAction) -> Unit,
) : BaseTracksViewModel() {

    init {
        getTracks()
    }

    override fun getTracks() {
        val tracks = trackRepository.getApiTracks(searchQuery = state.value.searchQuery)
            .cachedIn(viewModelScope)

        reduceState { copy(tracks = tracks) }
    }

    override fun onClickTrack(previewTrack: PreviewTrack) {
        onAction(
            ApiTracksViewModelAction.ClickTrack(
                trackId = previewTrack.id,
                position = previewTrack.position ?: 0
            )
        )
    }

    override fun onUpdateSearchQuery(search: String) {
        reduceState { copy(searchQuery = search) }

        searchQueryThrottledLambda()
    }

    override fun onClearSearchQuery() {
        reduceState { copy(searchQuery = "") }

        searchQueryThrottledLambda()
    }

    @AssistedFactory
    interface ApiTrackViewModelFactory {
        fun create(onAction: (ApiTracksViewModelAction) -> Unit): ApiTracksViewModel
    }
}