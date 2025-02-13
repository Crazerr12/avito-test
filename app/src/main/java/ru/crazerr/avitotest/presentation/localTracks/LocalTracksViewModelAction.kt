package ru.crazerr.avitotest.presentation.localTracks

sealed interface LocalTracksViewModelAction {
    data class ClickTrack(val trackId: Long) : LocalTracksViewModelAction
}