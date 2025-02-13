package ru.crazerr.avitotest.presentation.playbackTrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import ru.crazerr.avitotest.R
import ru.crazerr.avitotest.utils.components.ScreenLoading
import ru.crazerr.avitotest.utils.time.formatTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackView(modifier: Modifier = Modifier, viewModel: PlaybackViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(0),
                title = { Text(state.track.title, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleAction(PlaybackViewAction.BackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_content_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            ScreenLoading(modifier = Modifier.padding(paddingValues))
        } else {
            PlaybackViewContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                state = state,
                handleViewAction = viewModel::handleAction
            )
        }
    }

}

@Composable
private fun PlaybackViewContent(
    modifier: Modifier = Modifier,
    state: PlaybackState,
    handleViewAction: (PlaybackViewAction) -> Unit
) {
    Column(modifier = modifier) {
        TrackInfo(state = state)

        Spacer(modifier = Modifier.height(20.dp))

        PlaybackSlider(state = state, handleViewAction = handleViewAction)

        Spacer(modifier = Modifier.height(20.dp))

        PlaybackControlButtons(state = state, handleViewAction = handleViewAction)
    }
}

@Composable
private fun TrackInfo(state: PlaybackState) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(vertical = 20.dp),
        model = state.track.albumImage ?: state.track.trackImage,
        contentDescription = null,
        placeholder = painterResource(R.drawable.empty_image),
        error = painterResource(R.drawable.empty_image)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = state.track.title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 20.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    if (state.track.album != null) {
        Text(
            text = state.track.album,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
    }

    Text(
        text = state.track.author,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
}

@Composable
private fun PlaybackSlider(state: PlaybackState, handleViewAction: (PlaybackViewAction) -> Unit) {
    LaunchedEffect(state.isPlaying) {
        while (state.isPlaying) {
            withFrameMillis {
                handleViewAction(PlaybackViewAction.UpdateCurrentPosition)
            }
            delay(500)
        }
    }

    Slider(
        value = state.currentPosition.toFloat(),
        onValueChange = { handleViewAction(PlaybackViewAction.SeekTo(it.toLong())) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        valueRange = 0f..state.track.duration.toFloat(),
    )

    Spacer(modifier = Modifier.height(2.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = state.currentPosition.formatTime(),
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = state.track.duration.formatTime(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PlaybackControlButtons(
    modifier: Modifier = Modifier,
    state: PlaybackState,
    handleViewAction: (PlaybackViewAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { handleViewAction(PlaybackViewAction.SkipPrevious) }) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_previous),
                contentDescription = stringResource(R.string.skip_previous_content_description)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Button(
            onClick = { handleViewAction(PlaybackViewAction.ManagePlayback) },
            shape = CircleShape
        ) {
            Icon(
                painter = painterResource(if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = stringResource(if (state.isPlaying) R.string.play_content_description else R.string.pause_content_description)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(onClick = { handleViewAction(PlaybackViewAction.SkipNext) }) {
            Icon(
                painter = painterResource(R.drawable.ic_skip_next),
                contentDescription = stringResource(R.string.skip_next_content_description)
            )
        }
    }
}