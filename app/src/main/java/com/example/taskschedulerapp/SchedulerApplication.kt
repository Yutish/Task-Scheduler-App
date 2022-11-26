package com.example.taskschedulerapp

import android.app.Application

class SchedulerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SchedulerApplication
    }
}