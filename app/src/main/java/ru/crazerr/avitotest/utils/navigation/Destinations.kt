package ru.crazerr.avitotest.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object LocalTracks : Destination

    @Serializable
    data object ApiTracks : Destination

    @Serializable
    data class Playback(val trackId: Long, val isLocal: Boolean, val position: Int) : Destination
}