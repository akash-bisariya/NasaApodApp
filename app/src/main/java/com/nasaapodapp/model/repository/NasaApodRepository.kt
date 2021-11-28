package com.nasaapodapp.model.repository

import com.nasaapodapp.model.data.NasaApod
import com.nasaapodapp.model.data.network.NasaApodService
import com.nasaapodapp.model.data.network.NetworkResult
import javax.inject.Inject

class NasaApodRepository @Inject constructor(private val client: NasaApodService) {

    suspend fun getMovieDetail(date: String): NetworkResult<NasaApod> {
        return try {
            val response = client.getNasaApodData(date=date)
            if (response.isSuccessful)
                NetworkResult.Success(response.body() as NasaApod)
            else
                NetworkResult.Error("Network error")
        } catch (e: Exception) {
            NetworkResult.Error("Network error")
        }
    }


}