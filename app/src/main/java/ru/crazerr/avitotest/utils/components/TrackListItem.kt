package ru.crazerr.avitotest.utils.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.crazerr.avitotest.R
import ru.crazerr.avitotest.domain.model.Track

private const val MAX_TITLE_NAME = 1
private const val MAX_AUTHOR_NAME = 1

@Composable
fun TrackListItem(modifier: Modifier = Modifier, track: Track) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.height(50.dp),
            model = track.image,
            contentDescription = null,
            placeholder = painterResource(R.drawable.empty_image),
            error = painterResource(R.drawable.empty_image),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 6.dp)
        ) {
            Text(
                text = track.title,
                maxLines = MAX_TITLE_NAME,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = track.author,
                maxLines = MAX_AUTHOR_NAME,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}