package ru.crazerr.avitotest.domain.model

data class PreviewTrack(
    val id: Long,
    val image: String?,
    val title: String,
    val author: String,
    val position: Int? = null,
)