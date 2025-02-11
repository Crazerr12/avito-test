package ru.crazerr.avitotest.data.model

import kotlinx.serialization.Serializable
import ru.crazerr.avitotest.data.model.ApiChartTracksResponseBody.ApiChartTrackData.ApiChartTrack
import ru.crazerr.avitotest.domain.model.Track

@Serializable
internal data class ApiChartTracksResponseBody(
    val tracks: ApiChartTrackData
) {
    @Serializable
    internal data class ApiChartTrackData(
        val data: List<ApiChartTrack>
    ) {
        @Serializable
        internal data class ApiChartTrack(
            val id: Long,
            val title: String,
            val artist: ApiChartArtist,
            val album: ApiChartAlbum,
        ) {
            @Serializable
            internal data class ApiChartArtist(
                val name: String,
            )

            @Serializable
            internal data class ApiChartAlbum(
                val cover: String
            )
        }
    }
}

internal fun ApiChartTrack.toTrack() = Track(
    id = id,
    image = album.cover,
    title = title,
    author = artist.name
)

internal fun ApiChartTracksResponseBody.toTracks() = this.tracks.data.map { it.toTrack() }