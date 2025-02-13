package ru.crazerr.avitotest.data.model

import kotlinx.serialization.Serializable
import ru.crazerr.avitotest.data.model.ApiTracksResponseBody.ApiPreviewTrack
import ru.crazerr.avitotest.domain.model.PreviewTrack

@Serializable
internal data class ApiTracksResponseBody<T : Any>(
    val tracks: ResponseData<T>
) {
    @Serializable
    internal data class ApiPreviewTrack(
        val id: Long,
        val title: String,
        val artist: ApiArtist,
        val album: ApiAlbum,
    )
}

internal fun ApiPreviewTrack.toPreviewTrack() = PreviewTrack(
    id = id,
    image = album.cover,
    title = title,
    author = artist.name,
)
