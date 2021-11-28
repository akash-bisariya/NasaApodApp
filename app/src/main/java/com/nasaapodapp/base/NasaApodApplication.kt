package com.nasaapodapp.base

import android.app.Application
import com.nasaapodapp.di.DaggerApplicationComponent

class NasaApodApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}