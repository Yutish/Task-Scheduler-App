package com.example.taskschedulerapp.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.taskschedulerapp.R
import com.example.taskschedulerapp.databinding.FragmentClockBinding

/**
 * [ClockFragment] holds the main screen.
 */
class ClockFragment : Fragment() {

    private lateinit var clockFragmentViewModel: ClockFragmentViewModel

    private lateinit var clockBinding: FragmentClockBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //obtain an instance of the binding class
        clockBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_clock, container, false)

        //Getting the view model
        clockFragmentViewModel = ViewModelProvider(this)[ClockFragmentViewModel::class.java]

        clockBinding.clockFragmentViewModel = clockFragmentViewModel

        clockBinding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                clockFragmentViewModel.setDarkTheme()
            else
                clockFragmentViewModel.setLightTheme()
        }

        clockBinding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_clockFragment_to_taskFragment)
        }

        return clockBinding.root
    }

}