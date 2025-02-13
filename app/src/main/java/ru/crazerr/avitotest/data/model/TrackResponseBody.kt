package ru.crazerr.avitotest.data.model

import kotlinx.serialization.Serializable
import ru.crazerr.avitotest.domain.model.Track

@Serializable
internal data class ApiTrack(
    val id: Long,
    val title: String,
    val preview: String,
    val album: ApiAlbum,
    val artist: ApiArtist,
    val duration: Long,
)

@Serializable
internal data class TrackResponseBody(
    val tracks: ResponseData<List<ApiChartTrack>>
) {
    @Serializable
    internal data class ApiChartTrack(
        val id: Long,
        val position: Int,
    )
}

internal fun ApiTrack.toTracks() = Track(
    id = id,
    trackImage = null,
    albumImage = album.coverBig,
    title = title,
    author = artist.name,
    url = preview,
    duration = duration * 1000,
    album = album.title,
    albumId = album.id,
)