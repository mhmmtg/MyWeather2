package net.mguler.myweather2.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceSuggestAPI {

    @GET("autocomplete")
    suspend fun getPlaceSuggestion(
        @Query("text") text: String?,
        @Query("type") type: String?,
        @Query("bias") bias: String?,
        @Query("apiKey") api: String?
    ): Response<PlaceSuggestResponse>
}