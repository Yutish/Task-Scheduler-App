package com.example.taskschedulerapp

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat

class MainViewModel : ViewModel() {

    companion object {
        const val DATE_PATTERN = "EEEE, LLLL dd"
        const val TIME_PATTERN = "HH"
    }

    private val calendar = Calendar.getInstance()
    private val _currentDate = SimpleDateFormat(DATE_PATTERN).format(calendar.time)
    val currentDate: String
        get() = _currentDate

    private val time = SimpleDateFormat(TIME_PATTERN).format(calendar.time)
    val dayTime = when (time.toInt()) {
        in 6..17 -> "Today"
        else -> {
            "Tonight"
        }
    }

}