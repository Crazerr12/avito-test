package ru.crazerr.avitotest.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class ApiAlbum(
    val id: Long,
    val cover: String,
    @SerialName("cover_big") val coverBig: String,
    val title: String,
)