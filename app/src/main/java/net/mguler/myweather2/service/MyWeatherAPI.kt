package net.mguler.myweather2.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyWeatherAPI {

    @GET("forecast")
    suspend fun getWeatherData(
        @Query("appid") appid: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lng: Double?,
        @Query("units") units: String?
    ): Response<MyWeatherResponse>


}