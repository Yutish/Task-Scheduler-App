package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskschedulerapp.R
import com.example.taskschedulerapp.SchedulerApplication
import com.example.taskschedulerapp.database.getDatabase
import com.example.taskschedulerapp.databinding.FragmentClockBinding

/**
 * [ClockFragment] holds the main screen.
 */
class ClockFragment : Fragment() {

    private lateinit var clockFragmentViewModel: ClockFragmentViewModel

    private lateinit var clockBinding: FragmentClockBinding

    @SuppressLint("SetTextI18n", "SimpleDateFormat")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //obtain an instance of the binding class
        clockBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_clock, container, false)

        val taskDatabase = getDatabase(SchedulerApplication.instance)

        val clockFragmentViewModelFactory = ClockFragmentViewModelFactory(taskDatabase)

        //Getting the view model
        clockFragmentViewModel = ViewModelProvider(
            this, clockFragmentViewModelFactory
        )[ClockFragmentViewModel::class.java]

        clockBinding.clockFragmentViewModel = clockFragmentViewModel

        clockFragmentViewModel.fetchSingleData()


        clockBinding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) clockFragmentViewModel.setDarkTheme()
            else clockFragmentViewModel.setLightTheme()
        }

        clockBinding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_clockFragment_to_taskFragment)
        }

        clockFragmentViewModel.singleTask.observe(viewLifecycleOwner) {
            val simpleDateFormat = SimpleDateFormat("hh:mm a")
            if (it != null) {

                if (System.currentTimeMillis() > it.endTime) {
                    clockFragmentViewModel.deleteTask(it.id)
                } else {
                    if (System.currentTimeMillis() < it.startTime) {
                        beforeTask(it.id, it.startTime, it.endTime)
                    }
                    if (System.currentTimeMillis() in it.startTime..it.endTime) {
                        onGoingTask(it.id, it.endTime)
                    }

                    val startTimeInDate = simpleDateFormat.format(it.startTime)
                    val endTimeInDate = simpleDateFormat.format(it.endTime)
                    clockBinding.bottomSheetTitle.text =
                        "$startTimeInDate to $endTimeInDate - ${it.title}"
                    clockBinding.bottomSheetDesc.text = it.description
                }
            } else {
                clockBinding.centralTextView.text = "No Tasks"
                clockBinding.countDownTextView.text = "Scheduled"
            }
        }
        return clockBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun beforeTask(id: Long, startTime: Long, endTime: Long) {
        clockBinding.centralTextView.text = "Next task in"
        val time = (startTime - System.currentTimeMillis())
        object : CountDownTimer(time, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                clockBinding.countDownTextView.text =
                    "${millisUntilFinished / 60000}: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                onGoingTask(id, endTime)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun onGoingTask(id: Long, endTime: Long) {
        clockBinding.centralTextView.text = "Remaining"
        val time = (endTime - System.currentTimeMillis())
        object : CountDownTimer(time, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                clockBinding.countDownTextView.text = "${(millisUntilFinished / 1000) / 60} minutes"
            }

            override fun onFinish() {
                clockFragmentViewModel.deleteTask(id)
                clockFragmentViewModel.fetchSingleData()

            }
        }.start()
    }


}