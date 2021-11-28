package com.nasaapodapp.model.data.network

import com.nasaapodapp.BuildConfig
import com.nasaapodapp.model.data.NasaApod
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApodService {

    @GET("planetary/apod")
    suspend fun getNasaApodData(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("date") date: String = "2021-11-25",
    ): Response<NasaApod>

}