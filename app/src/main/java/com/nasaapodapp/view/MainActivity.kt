package com.nasaapodapp.view

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.nasaapodapp.R
import com.nasaapodapp.base.NasaApodApplication
import com.nasaapodapp.base.getViewModel
import com.nasaapodapp.model.repository.NasaApodRepository
import com.nasaapodapp.viewmodel.NasaApodViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var nasaApodViewModel: NasaApodViewModel

    @Inject
    lateinit var nasaApodRepository: NasaApodRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as NasaApodApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nasaApodViewModel = getViewModel { NasaApodViewModel(nasaApodRepository) }

        nasaApodViewModel.nasaApodData.observe(this, Observer {
            Picasso.get().load(it.url).into(iv_apod)
            tv_title.text = it.title
            tv_date.text = it.date
            tv_explanation.text = it.explanation
        })

        btn_switch.setOnClickListener{
            val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (isNightTheme) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }
}