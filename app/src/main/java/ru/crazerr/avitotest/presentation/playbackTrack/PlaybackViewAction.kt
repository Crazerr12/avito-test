package ru.crazerr.avitotest.presentation.playbackTrack

sealed interface PlaybackViewAction {
    data object ManagePlayback : PlaybackViewAction
    data object BackClick : PlaybackViewAction

    data object SkipNext : PlaybackViewAction
    data object SkipPrevious : PlaybackViewAction

    data class SeekTo(val positionMs: Long) : PlaybackViewAction

    data object UpdateCurrentPosition : PlaybackViewAction
}