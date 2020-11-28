package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.databinding.ActivityDetailsBinding
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var initialStation: Station
    private lateinit var viewModel: DetailsViewModel

    @Inject
    lateinit var stationRepository: StationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialStation = intent.getParcelableExtra(MainActivity.STATION_MESSAGE)!!
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val viewModel by viewModels<DetailsViewModel> {
            DetailsViewModel.Factory(initialStation, stationRepository)
        }
        this.viewModel = viewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}
