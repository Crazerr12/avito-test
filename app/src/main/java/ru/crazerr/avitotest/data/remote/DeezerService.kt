package ru.crazerr.avitotest.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.crazerr.avitotest.data.model.ApiPreviewChartTrack
import ru.crazerr.avitotest.data.model.ApiTrack
import ru.crazerr.avitotest.data.model.ApiTracksResponseBody
import ru.crazerr.avitotest.data.model.ResponseData
import ru.crazerr.avitotest.data.model.TrackResponseBody

internal interface DeezerService {

    @GET("/chart")
    suspend fun getChart(
        @Query("index") offset: Int,
        @Query("limit") limit: Int
    ): ApiTracksResponseBody<List<ApiPreviewChartTrack>>

    @GET("/search")
    suspend fun getSearch(
        @Query("q") query: String,
        @Query("index") offset: Int,
        @Query("limit") limit: Int,
    ): ResponseData<List<ApiTracksResponseBody.ApiPreviewTrack>>

    @GET("/track/{id}")
    suspend fun getTrackById(
        @Path("id") id: Long,
    ): ApiTrack

    @GET("/chart")
    suspend fun getTrackQueueByChart(
        @Query("index") offset: Int,
        @Query("limit") limit: Int,
    ): TrackResponseBody
}