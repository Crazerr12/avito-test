package ru.crazerr.avitotest.utils.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.crazerr.avitotest.R

@Composable
fun ErrorView(modifier: Modifier = Modifier, error: String, onRetry: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = error,
            style = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(onClick = onRetry) {
            Text(
                text = stringResource(R.string.error_retry),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}