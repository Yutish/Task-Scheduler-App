package com.example.taskschedulerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.taskschedulerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting view and obtain an instance of the binding class
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Getting the view model
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.mainViewModel = viewModel

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.setDarkTheme()
            else
                viewModel.setLightTheme()
        }
    }
}