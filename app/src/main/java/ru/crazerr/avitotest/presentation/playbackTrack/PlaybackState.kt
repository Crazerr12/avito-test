package ru.crazerr.avitotest.presentation.playbackTrack

import ru.crazerr.avitotest.domain.model.Track

data class PlaybackState(
    val track: Track,
    val isLoading: Boolean,
    val isPlaying: Boolean,
    val currentPosition: Long,
)

internal val InitialPlaybackState = PlaybackState(
    track = Track(
        id = 0,
        trackImage = null,
        title = "",
        author = "",
        album = null,
        url = "",
        duration = 0L,
        albumImage = null,
        albumId = 0L
    ),
    isLoading = true,
    isPlaying = false,
    currentPosition = 0L,
)