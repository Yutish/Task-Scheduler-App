package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskschedulerapp.SingletonCalender
import com.example.taskschedulerapp.database.Task
import com.example.taskschedulerapp.database.TaskDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ClockFragmentViewModel(database: TaskDatabase) : ViewModel() {
    companion object {
        const val DATE_PATTERN = "EEEE, LLLL dd"
        const val TIME_PATTERN = "HH"
    }

    private val calendar = SingletonCalender.calendarInstace
    private val taskDao = database.taskDAO

    private lateinit var _singleTask : LiveData<Task>
    val singleTask: LiveData<Task>
    get() = _singleTask

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

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchSingleData(){
        viewModelScope.launch {
            _singleTask = taskDao.getSingleTask()
            Log.i("YUtishs", _singleTask.toString())
        }
    }


}