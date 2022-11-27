package com.example.taskschedulerapp.clock

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //obtain an instance of the binding class
        clockBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_clock, container, false)

        val taskDatabase = getDatabase(SchedulerApplication.instance)

        val clockFragmentViewModelFactory = ClockFragmentViewModelFactory(taskDatabase)

        //Getting the view model
        clockFragmentViewModel = ViewModelProvider(
            this,
            clockFragmentViewModelFactory
        )[ClockFragmentViewModel::class.java]

        clockBinding.clockFragmentViewModel = clockFragmentViewModel

        clockFragmentViewModel.fetchSingleData()


        clockBinding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                clockFragmentViewModel.setDarkTheme()
            else
                clockFragmentViewModel.setLightTheme()
        }

        clockBinding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_clockFragment_to_taskFragment)
        }

        clockFragmentViewModel.singleTask.observe(viewLifecycleOwner) {
            val simpleDateFormat = SimpleDateFormat("hh:mm a")
            if (it != null ) {
                val startTimeInDate = simpleDateFormat.format(it.startTime)
                val endTimeInDate = simpleDateFormat.format(it.endTime)
                clockBinding.bottomSheetTitle.text =
                    "$startTimeInDate to $endTimeInDate - ${it.title}"
                clockBinding.bottomSheetDesc.text = it.description
            }

        }

        return clockBinding.root
    }

}