package com.nasaapodapp.di

import com.nasaapodapp.HomeActivity
import com.nasaapodapp.view.MainActivity
import com.nasaapodapp.model.data.network.ApiModule
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

}