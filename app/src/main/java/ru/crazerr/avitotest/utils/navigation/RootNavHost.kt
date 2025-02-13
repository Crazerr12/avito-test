package ru.crazerr.avitotest.utils.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.crazerr.avitotest.feature.baseTracks.BaseTracksView
import ru.crazerr.avitotest.presentation.apiTracks.ApiTracksViewModel
import ru.crazerr.avitotest.presentation.apiTracks.ApiTracksViewModelAction
import ru.crazerr.avitotest.presentation.localTracks.LocalTracksViewModel
import ru.crazerr.avitotest.presentation.localTracks.LocalTracksViewModelAction
import ru.crazerr.avitotest.presentation.playbackTrack.PlaybackView
import ru.crazerr.avitotest.presentation.playbackTrack.PlaybackViewModel
import ru.crazerr.avitotest.presentation.playbackTrack.PlaybackViewModelAction

@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destination.LocalTracks) {
        composable<Destination.LocalTracks> {
            BaseTracksView(viewModel = hiltViewModel<LocalTracksViewModel, LocalTracksViewModel.LocalTracksViewModelFactory> { factory ->
                factory.create(onAction = { action ->
                    when (action) {
                        is LocalTracksViewModelAction.ClickTrack -> navController.navigate(
                            Destination.Playback(
                                trackId = action.trackId,
                                isLocal = true,
                                position = 0
                            )
                        )
                    }
                })
            })
        }

        composable<Destination.ApiTracks> {
            BaseTracksView(viewModel = hiltViewModel<ApiTracksViewModel, ApiTracksViewModel.ApiTrackViewModelFactory> { factory ->
                factory.create(onAction = { action ->
                    when (action) {
                        is ApiTracksViewModelAction.ClickTrack -> navController.navigate(
                            Destination.Playback(
                                trackId = action.trackId,
                                isLocal = false,
                                position = action.position
                            )
                        )
                    }
                })
            })
        }

        composable<Destination.Playback> { backStackEntry ->
            val playback: Destination.Playback = backStackEntry.toRoute()
            PlaybackView(viewModel = hiltViewModel<PlaybackViewModel, PlaybackViewModel.PlaybackViewModelFactory> { factory ->
                factory.create(
                    onAction = { action ->
                        when (action) {
                            PlaybackViewModelAction.BackClick -> navController.popBackStack()
                        }
                    },
                    initialTrackId = playback.trackId,
                    isLocal = playback.isLocal,
                    position = playback.position
                )
            })
        }
    }
}