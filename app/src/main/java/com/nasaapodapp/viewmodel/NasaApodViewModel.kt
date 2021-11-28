package com.nasaapodapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasaapodapp.model.data.NasaApod
import com.nasaapodapp.model.data.network.NetworkResult
import com.nasaapodapp.model.repository.NasaApodRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NasaApodViewModel @Inject constructor(private val nasaApodRepository: NasaApodRepository
//                                               , @Assisted id:String
) : ViewModel() {
    val nasaApodData = MutableLiveData<NasaApod>()
    val error = MutableLiveData<String>()

    init {
        getNasaApodData("2021-11-25")
    }

    fun getNasaApodData(date:String){
        viewModelScope.launch {
            when (val result = nasaApodRepository.getMovieDetail(date)) {
                is NetworkResult.Success -> nasaApodData.postValue(result.data)
                is NetworkResult.Error -> error.postValue(result.errorMessage)
            }
        }
    }

}