package ru.crazerr.avitotest.feature.baseTracks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import ru.crazerr.avitotest.R
import ru.crazerr.avitotest.utils.components.ErrorView
import ru.crazerr.avitotest.utils.components.PagingLazyColumn
import ru.crazerr.avitotest.utils.components.TrackListItem

@Composable
fun BaseTracksView(modifier: Modifier = Modifier, viewModel: BaseTracksViewModel) {
    val state by viewModel.state.collectAsState()
    Column(modifier = modifier) {
        SearchInput(searchInput = state.searchQuery, handleViewAction = viewModel::handleAction)

        TrackPagingColumn(modifier = Modifier.weight(1f), state = state)
    }
}

@Composable
private fun SearchInput(
    modifier: Modifier = Modifier,
    searchInput: String,
    handleViewAction: (BaseTracksViewAction) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        value = searchInput,
        onValueChange = { handleViewAction(BaseTracksViewAction.UpdateSearchQuery(it)) },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = if (searchInput.isNotBlank()) {
            {
                IconButton(onClick = { handleViewAction(BaseTracksViewAction.ClearSearchQuery) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.clear_search_content_description)
                    )
                }
            }
        } else {
            null
        },
        placeholder = { Text(text = stringResource(R.string.default_search_hint)) }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrackPagingColumn(
    modifier: Modifier = Modifier,
    state: BaseTracksState,
) {
    val tracks = state.tracks.collectAsLazyPagingItems()

    PagingLazyColumn(
        modifier = modifier.imePadding(),
        loadStates = tracks.loadState,
        isEmpty = tracks.itemCount < 1,
        error = { title, description ->
            ErrorView(error = title, onRetry = { tracks.retry() })
        },
        errorItem = {
            ErrorView(
                error = stringResource(R.string.track_screen_error_item),
                onRetry = { tracks.retry() })
        },
        empty = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = stringResource(R.string.track_screen_empty))
            }
        },
    ) {
        items(tracks.itemCount) {
            val track = tracks[it]
            if (track != null) {
                TrackListItem(track = track)
            }
        }
    }
}
