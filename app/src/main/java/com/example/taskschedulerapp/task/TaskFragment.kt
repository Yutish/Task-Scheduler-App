package com.example.taskschedulerapp.task

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskschedulerapp.R
import com.example.taskschedulerapp.SchedulerApplication
import com.example.taskschedulerapp.SingletonCalender
import com.example.taskschedulerapp.database.getDatabase
import com.example.taskschedulerapp.databinding.FragmentTaskBinding
import kotlin.properties.Delegates


/**
 *  [TaskFragment] used to add task
 */
class TaskFragment : Fragment() {

    companion object {
        const val TIME_FORMAT = "hh:mm a"
    }

    private lateinit var taskFragmentViewModel: TaskFragmentViewModel

    private lateinit var taskBinding: FragmentTaskBinding

    private val calendar = SingletonCalender.calendarInstace

    private var startTimeInLong by Delegates.notNull<Long>()
    private var endTimeInLong by Delegates.notNull<Long>()


    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        taskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        val taskDatabase = getDatabase(SchedulerApplication.instance)

        val taskFragmentViewModelFactory = TaskFragmentViewModelFactory(taskDatabase)

        taskFragmentViewModel =
            ViewModelProvider(this, taskFragmentViewModelFactory)[TaskFragmentViewModel::class.java]

        taskBinding.taskFragmentViewModel = taskFragmentViewModel

        taskBinding.startTimeButton.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context, { _, hourOfDay, minutes ->
                    val selectedTime = calendar
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minutes)
                    taskBinding.startTimeTextView.text =
                        SimpleDateFormat(TIME_FORMAT).format(selectedTime.time)
                    startTimeInLong = selectedTime.time.time
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            timePickerDialog.show()
        }

        taskBinding.endTimeButton.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context, { _, hourOfDay, minutes ->
                    val selectedTime = calendar
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minutes)
                    taskBinding.endTimeTextView.text =
                        SimpleDateFormat(TIME_FORMAT).format(selectedTime.time)
                    endTimeInLong = selectedTime.time.time
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            timePickerDialog.show()
        }

        taskBinding.doneButton.setOnClickListener {

            val text = taskBinding.titleTextView.text.toString()
            val desc = taskBinding.descTextView.text.toString()

            if (TextUtils.isEmpty(taskBinding.startTimeTextView.text.toString()) || TextUtils.isEmpty(
                    taskBinding.endTimeTextView.text.toString()
                ) || TextUtils.isEmpty(taskBinding.titleTextView.text.toString()) || TextUtils.isEmpty(
                    taskBinding.descTextView.text.toString()
                )
            ) {
                Toast.makeText(context, "Please enter all the fields", Toast.LENGTH_SHORT).show()

            } else {
                if (startTimeInLong >= endTimeInLong) {

                    Toast.makeText(
                        context, "End time should be greater than start time", Toast.LENGTH_SHORT
                    ).show()
                } else if (System.currentTimeMillis() > startTimeInLong || System.currentTimeMillis() > endTimeInLong) {
                    Toast.makeText(
                        context,
                        "Start and end time should be greater than current time",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    taskFragmentViewModel.addInDB(startTimeInLong, endTimeInLong, text, desc)
                    findNavController().navigate(R.id.action_taskFragment_to_clockFragment)
                }
            }
        }

        return taskBinding.root
    }


}