package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskschedulerapp.R
import com.example.taskschedulerapp.SchedulerApplication
import com.example.taskschedulerapp.database.getDatabase
import com.example.taskschedulerapp.databinding.FragmentClockBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_clock.*

/**
 * [ClockFragment] holds the main screen.
 */
class ClockFragment : Fragment() {

    private lateinit var clockFragmentViewModel: ClockFragmentViewModel

    private lateinit var clockBinding: FragmentClockBinding

    private val sharedPreferences =
        SchedulerApplication.instance.getSharedPreferences("SharedPref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    @RequiresApi(Build.VERSION_CODES.R)
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


        clockBinding.themeSwitch.isChecked = sharedPreferences.getBoolean(SWITCH_VALUE_STRING, true)

        if (clockBinding.themeSwitch.isChecked) clockFragmentViewModel.setDarkTheme()
        else clockFragmentViewModel.setLightTheme()

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

                //When there is no task scheduled
                clockBinding.apply {
                    centralTextView.text = "No Tasks"
                    countDownTextView.text = "Scheduled"
                    bottomSheetTitle.text = ""
                    bottomSheetDesc.text = ""
                }

            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(clockBinding.bottomSheet)

        clockBinding.bottomSheetUp.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            clockBinding.apply {
                bottomSheetUp.visibility = View.GONE
                bottomSheetDown.visibility = View.VISIBLE
            }

        }
        clockBinding.bottomSheetDown.setOnClickListener {

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            clockBinding.apply {
                bottomSheetUp.visibility = View.VISIBLE
                bottomSheetDown.visibility = View.GONE
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
                if (millisUntilFinished < 60000) {
                    clockBinding.countDownTextView.text = "${millisUntilFinished / 1000} seconds"
                } else {
                    clockBinding.countDownTextView.text = "${millisUntilFinished / 60000}+ minutes"
                }
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
                if (millisUntilFinished < 60000) {
                    clockBinding.countDownTextView.text = "${millisUntilFinished / 1000} seconds"
                } else {
                    clockBinding.countDownTextView.text = "${millisUntilFinished / 60000}+ minutes"
                }
            }

            override fun onFinish() {
                clockFragmentViewModel.apply {
                    deleteTask(id)
                    fetchSingleData()
                }


            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editor.apply {
            putBoolean(SWITCH_VALUE_STRING, clockBinding.themeSwitch.isChecked)
        }.apply()
    }

    companion object {
        const val SWITCH_VALUE_STRING = "switchValue"
    }

}