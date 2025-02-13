package ru.crazerr.avitotest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T : Any>(
    val data: T,
)
