package ru.crazerr.avitotest.utils.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.crazerr.avitotest.feature.baseTracks.BaseTracksView
import ru.crazerr.avitotest.presentation.localTracks.LocalTracksViewModel

@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Destination.LocalTracks) {
        composable<Destination.LocalTracks> { BaseTracksView(viewModel = hiltViewModel<LocalTracksViewModel>()) }

        composable<Destination.ApiTracks> {}
    }
}