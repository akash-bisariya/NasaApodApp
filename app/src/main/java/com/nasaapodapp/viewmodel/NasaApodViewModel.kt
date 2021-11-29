package com.nasaapodapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasaapodapp.model.data.NasaApod
import com.nasaapodapp.model.data.network.NetworkResult
import com.nasaapodapp.model.repository.NasaApodRepository
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class NasaApodViewModel @Inject constructor(private val nasaApodRepository: NasaApodRepository) : ViewModel() {
    val nasaApodData = MutableLiveData<NasaApod>()
    val error = MutableLiveData<String>()

    fun getNasaApodData(date:String){
        viewModelScope.launch {
            when (val result = nasaApodRepository.getMovieDetail(date)) {
                is NetworkResult.Success -> nasaApodData.postValue(result.data)
                is NetworkResult.Error -> error.postValue(result.errorMessage)
            }
        }
    }

}