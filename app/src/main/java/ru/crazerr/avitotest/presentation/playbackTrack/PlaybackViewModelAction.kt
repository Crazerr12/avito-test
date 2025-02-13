package ru.crazerr.avitotest.presentation.playbackTrack

sealed interface PlaybackViewModelAction {
    data object BackClick : PlaybackViewModelAction
}