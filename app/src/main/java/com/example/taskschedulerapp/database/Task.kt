package com.example.taskschedulerapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val startTime: Long,
    val endTime: Long,
    val title: String,
    val description: String
)
