package com.example.taskschedulerapp.task

import androidx.lifecycle.ViewModel
import com.example.taskschedulerapp.database.Task
import com.example.taskschedulerapp.database.TaskDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskFragmentViewModel(database: TaskDatabase) : ViewModel() {

    private val dao = database.taskDAO

    @OptIn(DelicateCoroutinesApi::class)
    fun addInDB(startTime: Long, endTime: Long, title: String, description: String) {
        GlobalScope.launch {
            dao.insertTask(
                Task(
                    id = 0,
                    startTime = startTime,
                    endTime = endTime,
                    title = title,
                    description = description
                )
            )
        }

    }

}