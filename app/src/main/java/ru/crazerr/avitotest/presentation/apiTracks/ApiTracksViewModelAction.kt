package ru.crazerr.avitotest.presentation.apiTracks

sealed interface ApiTracksViewModelAction {
    data class ClickTrack(val trackId: Long, val position: Int) : ApiTracksViewModelAction
}