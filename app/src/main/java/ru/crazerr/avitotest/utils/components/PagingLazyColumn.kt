package ru.crazerr.avitotest.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

private val CircularProgressIndicatorStrokeWidth = 2.dp
private val CircularProgressIndicatorStrokeSize = 18.dp

@Composable
fun PagingLazyColumn(
    modifier: Modifier = Modifier,
    loadStates: CombinedLoadStates,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isEmpty: Boolean,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    loading: @Composable () -> Unit = { ScreenLoading() },
    error: @Composable (String, String) -> Unit,
    empty: @Composable () -> Unit,
    loadingItem: @Composable () -> Unit = { PagingLazyColumnItemLoading() },
    errorItem: @Composable () -> Unit,
    listContent: LazyListScope.() -> Unit,
) {
    val loadStateRefresh = loadStates.refresh
    val loadStateAppend = loadStates.append

    when {
        loadStateRefresh is LoadState.Loading -> loading()
        loadStateRefresh is LoadState.Error -> {
            error(
                loadStateRefresh.error.message.orEmpty(),
                loadStateRefresh.error.cause.toString()
            )
        }

        loadStateRefresh is LoadState.NotLoading && isEmpty -> empty()
        loadStateRefresh is LoadState.NotLoading -> LazyColumn(
            modifier = Modifier.then(modifier),
            state = state,
            contentPadding = contentPadding,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            if (reverseLayout) {
                item {
                    if (loadStateAppend is LoadState.Error) {
                        errorItem()
                    }
                }
                item {
                    if (loadStateAppend is LoadState.Loading) {
                        loadingItem()
                    }
                }
            }
            listContent()
            if (!reverseLayout) {
                item {
                    if (loadStateAppend is LoadState.Error) {
                        errorItem()
                    }
                }
                item {
                    if (loadStateAppend is LoadState.Loading) {
                        loadingItem()
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(CircularProgressIndicatorStrokeSize),
            strokeWidth = CircularProgressIndicatorStrokeWidth,
        )
    }
}

@Composable
private fun PagingLazyColumnItemLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(CircularProgressIndicatorStrokeSize),
            strokeWidth = CircularProgressIndicatorStrokeWidth,
        )
    }
}

