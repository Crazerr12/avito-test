package ru.crazerr.avitotest.presentation.localTracks

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.domain.repository.TrackRepository
import ru.crazerr.avitotest.feature.baseTracks.BaseTracksViewModel

@HiltViewModel(assistedFactory = LocalTracksViewModel.LocalTracksViewModelFactory::class)
class LocalTracksViewModel @AssistedInject constructor(
    private val trackRepository: TrackRepository,
    @Assisted private val onAction: (LocalTracksViewModelAction) -> Unit,
) : BaseTracksViewModel() {

    init {
        getTracks()
    }

    override fun getTracks() {
        val tracks = trackRepository.getLocalTracks(searchQuery = state.value.searchQuery)
            .cachedIn(viewModelScope)

        reduceState { copy(tracks = tracks) }
    }

    override fun onClickTrack(previewTrack: PreviewTrack) {
        onAction(LocalTracksViewModelAction.ClickTrack(trackId = previewTrack.id))
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
    interface LocalTracksViewModelFactory {
        fun create(onAction: (LocalTracksViewModelAction) -> Unit): LocalTracksViewModel
    }
}