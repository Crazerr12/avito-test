package ru.crazerr.avitotest.domain.model

data class Track(
    val id: Long,
    val trackImage: String?,
    val albumImage: String?,
    val title: String,
    val author: String,
    val albumId: Long,
    val album: String?,
    val url: String,
    val duration: Long,
)