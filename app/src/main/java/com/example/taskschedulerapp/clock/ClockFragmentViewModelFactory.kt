package com.example.taskschedulerapp.clock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskschedulerapp.database.TaskDatabase

class ClockFragmentViewModelFactory(
    private val database: TaskDatabase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClockFragmentViewModel::class.java)) {
            return ClockFragmentViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}