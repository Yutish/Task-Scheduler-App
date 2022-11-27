package com.example.taskschedulerapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {

    @Insert
    fun insertTask(task: Task)

    @Query("DELETE FROM task WHERE id=:id")
    fun deleteTask(id: Long)

    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task ORDER BY startTime ASC LIMIT 1")
    fun getSingleTask(): LiveData<Task>
}

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val taskDAO: TaskDAO

}

private lateinit var INSTANCE: TaskDatabase

fun getDatabase(context: Context): TaskDatabase {
    synchronized(TaskDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_database"
            ).build()
        }
    }
    return INSTANCE
}
