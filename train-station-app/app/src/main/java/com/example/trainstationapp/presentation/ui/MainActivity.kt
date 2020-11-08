package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.R
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var stationRepository: StationRepository

    val viewModel by viewModels<MainViewModel>{ MainViewModel.Factory(stationRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }
}
