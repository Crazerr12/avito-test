package ru.crazerr.avitotest.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.crazerr.avitotest.data.model.ApiChartTracksResponseBody

internal interface DeezerService {

    @GET("/chart")
    suspend fun getChart(
        @Query("index") offset: Int,
        @Query("limit") limit: Int
    ): ApiChartTracksResponseBody

    @GET("/search")
    suspend fun getSearch(
        @Query("q") query: String,
        @Query("index") offset: Int,
        @Query("limit") limit: Int,
    ): ApiChartTracksResponseBody.ApiChartTrackData
}