package com.nasaapodapp.view

import android.R.attr
import android.app.DatePickerDialog
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
import android.R.attr.startYear
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    @Inject
    lateinit var nasaApodViewModel: NasaApodViewModel

    @Inject
    lateinit var nasaApodRepository: NasaApodRepository


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as NasaApodApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nasaApodViewModel = getViewModel { NasaApodViewModel(nasaApodRepository) }
        nasaApodViewModel.getNasaApodData(getTodayDate())

        nasaApodViewModel.nasaApodData.observe(this, Observer {
            Picasso.get().load(it.url).into(iv_apod)
            tv_title.text = it.title
            tv_date.text = it.date
            tv_explanation.text = it.explanation
        })

        btn_search.setOnClickListener {
            val cal = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this, this@MainActivity,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        btn_switch.setOnClickListener{
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        var dateFormat= SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(calendar.time)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth

        var dateFormat= SimpleDateFormat("yyyy-MM-dd")

        nasaApodViewModel.getNasaApodData(dateFormat.format(mCalendar.time))

    }
}