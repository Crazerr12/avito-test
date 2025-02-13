package ru.crazerr.avitotest.data.model

import kotlinx.serialization.Serializable
import ru.crazerr.avitotest.domain.model.PreviewTrack

@Serializable
internal data class ApiPreviewChartTrack(
    val id: Long,
    val title: String,
    var artist: ApiArtist,
    val album: ApiAlbum,
    val position: Int,
)

internal fun ApiPreviewChartTrack.toPreviewTrack() = PreviewTrack(
    id = id,
    title = title,
    author = artist.name,
    position = position,
    image = album.cover
)