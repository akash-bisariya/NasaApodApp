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
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.view.View
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.nasaapodapp.model.data.NasaApod
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var swicthingTheme: Boolean = false
    private var mNasaApodData:NasaApod? =null

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
        pb_loading.visibility = View.VISIBLE

        nasaApodViewModel.nasaApodData.observe(this, Observer {
            pb_loading.visibility = View.GONE
            mNasaApodData = it
            Picasso.get().load(it.url).into(iv_apod)
            tv_title.text = it.title
            tv_date.text = it.date
            tv_explanation.text = it.explanation
            iv_heart.visibility = View.VISIBLE

        })

        nasaApodViewModel.error.observe(this, Observer {
            pb_loading.visibility = View.GONE
            iv_heart.visibility = View.GONE
            if(!swicthingTheme)
                showMessageDialog(getString(R.string.txt_error))
        })

        btn_search.setOnClickListener {
            val cal = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this, this@MainActivity,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        iv_heart.setOnClickListener {
            saveToSharedPreference()
        }

        btn_switch.setOnClickListener{
            swicthingTheme = true
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }

    private fun showMessageDialog(s: String) {
        AlertDialog.Builder(this).setTitle(s).setPositiveButton("ok",
            { dialogInterface, i ->  dialogInterface.cancel() }).show()
    }

    /**
     * Saving favourites data
     */
    private fun saveToSharedPreference() {
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString(getString(R.string.apod_image), mNasaApodData!!.url)
            putString(getString(R.string.apod_title), mNasaApodData!!.title)
            putString(getString(R.string.apod_date), mNasaApodData!!.date)
            putString(getString(R.string.apod_expl), mNasaApodData!!.explanation)
            apply()
        }

        showMessageDialog(getString(R.string.txt_saved_data))
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

        ngit asaApodViewModel.getNasaApodData(dateFormat.format(mCalendar.time))
        pb_loading.visibility = View.VISIBLE

    }
}