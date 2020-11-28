package com.example.trainstationapp.data.datasources

import com.example.trainstationapp.data.models.Paginated
import com.example.trainstationapp.data.models.StationModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TrainStationDataSource {
    companion object {
        const val BASE_URL = "https://train-station.cleverapps.io/"
        const val CONTENT_TYPE = "application/json"
    }

    /**
     * Selects resource fields.
     *
     * @param s Adds search condition.
     * @param limit Limit amount of resources.
     * @param page Page portion of resources.
     */
    @GET("stations")
    suspend fun find(
        @Query("s") s: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
    ): Paginated<StationModel>

    @POST("stations")
    suspend fun create(@Body body: StationModel): StationModel

    @GET("stations/{id}")
    suspend fun findById(@Path("id") id: String): StationModel?

    @PATCH("stations/{id}")
    suspend fun updateById(@Path("id") id: String, @Body body: StationModel): StationModel?
}
