package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.taskschedulerapp.SingletonCalender
import java.text.SimpleDateFormat

class ClockFragmentViewModel : ViewModel() {
    companion object {
        const val DATE_PATTERN = "EEEE, LLLL dd"
        const val TIME_PATTERN = "HH"
    }

    private val calendar = SingletonCalender.calendarInstace
    @SuppressLint("SimpleDateFormat")
    private val _currentDate = SimpleDateFormat(DATE_PATTERN).format(calendar.time)
    val currentDate: String
        get() = _currentDate

    @SuppressLint("SimpleDateFormat")
    private val time = SimpleDateFormat(TIME_PATTERN).format(calendar.time)
    val dayTime = when (time.toInt()) {
        in 6..17 -> "Today"
        else -> {
            "Tonight"
        }
    }

    fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}