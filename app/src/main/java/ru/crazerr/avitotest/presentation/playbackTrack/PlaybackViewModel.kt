package ru.crazerr.avitotest.presentation.playbackTrack

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.crazerr.avitotest.domain.repository.TrackRepository
import ru.crazerr.avitotest.utils.presentation.MviViewModel

@HiltViewModel(assistedFactory = PlaybackViewModel.PlaybackViewModelFactory::class)
class PlaybackViewModel @AssistedInject constructor(
    private val trackRepository: TrackRepository,
    private val exoPlayer: ExoPlayer,
    @Assisted private val onAction: (PlaybackViewModelAction) -> Unit,
    @Assisted private val initialTrackId: Long,
    @Assisted private val isLocal: Boolean,
    @Assisted private val position: Int,
) : MviViewModel<PlaybackState, PlaybackViewAction>(InitialPlaybackState) {

    private var albumTracks: List<Long> = listOf(initialTrackId)

    init {

        getTrack(initialTrackId)
        observeExoPlayer()
    }

    override fun handleAction(action: PlaybackViewAction) {
        when (action) {
            PlaybackViewAction.ManagePlayback -> onManagePlayback()
            PlaybackViewAction.BackClick -> onBackClick()
            PlaybackViewAction.SkipNext -> onSkipNext()
            PlaybackViewAction.SkipPrevious -> onSkipPrevious()
            is PlaybackViewAction.SeekTo -> {
                exoPlayer.seekTo(action.positionMs)
                onUpdateCurrentPosition()
            }

            PlaybackViewAction.UpdateCurrentPosition -> onUpdateCurrentPosition()
        }
    }

    private fun onSkipNext() {
        if (isLocal) {
            getTrack(id = state.value.track.id + 1)
        } else {
            val index = albumTracks.indexOfFirst { state.value.track.id == it }

            if (index == albumTracks.lastIndex) {
                getTrack(albumTracks.first())
            } else {
                getTrack(albumTracks[index + 1])
            }
        }
    }

    private fun onSkipPrevious() {
        if (isLocal) {
            getTrack(id = state.value.track.id - 1)
        } else {
            val index = albumTracks.indexOfFirst { state.value.track.id == it }

            if (index == 0) {
                getTrack(albumTracks.last(), isPrevious = true)
            } else {
                getTrack(albumTracks[index - 1])
            }
        }
    }

    private fun getTrack(id: Long, isPrevious: Boolean = false) {
        viewModelScope.launch {
            reduceState { copy(isLoading = true) }
            val result = if (isLocal) {
                trackRepository.getLocalTrackById(id = id)
            } else {
                val track = trackRepository.getApiTrackById(id = id)

                val currentIndex = albumTracks.indexOfFirst { it == id }
                if (currentIndex + 1 == albumTracks.size && track.isSuccess && !isPrevious) {
                    val tracks =
                        trackRepository.getTrackQueueByChart(position + albumTracks.size - 1)

                    tracks.fold(
                        onSuccess = { albumTracks = albumTracks + it },
                        onFailure = { albumTracks = listOf(initialTrackId) },
                    )
                }

                track
            }

            result.fold(
                onSuccess = {
                    reduceState { copy(track = it, isLoading = false, currentPosition = 0L) }
                    exoPlayer.apply {
                        setMediaItem(MediaItem.fromUri(state.value.track.url))
                        playWhenReady = true
                        prepare()
                    }
                },
                onFailure = { reduceState { copy(isLoading = false) } },
            )
        }
    }

    private fun observeExoPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                reduceState { copy(isPlaying = isPlaying) }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    reduceState {
                        copy(
                            track = track.copy(duration = exoPlayer.duration),
                            isPlaying = true
                        )
                    }
                }
            }
        })
    }

    private fun onUpdateCurrentPosition() {
        reduceState { copy(currentPosition = exoPlayer.currentPosition) }
    }

    private fun onManagePlayback() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            reduceState { copy(isPlaying = false) }
        } else {
            exoPlayer.play()
            reduceState { copy(isPlaying = true) }
        }
    }

    private fun onBackClick() {
        onAction(PlaybackViewModelAction.BackClick)
    }

    override fun onCleared() {
        if (exoPlayer.isPlaying) {
            exoPlayer.stop()
        }
        exoPlayer.clearMediaItems()
        super.onCleared()
    }

    @AssistedFactory
    interface PlaybackViewModelFactory {
        fun create(
            onAction: (PlaybackViewModelAction) -> Unit,
            initialTrackId: Long,
            isLocal: Boolean,
            position: Int,
        ): PlaybackViewModel
    }
}