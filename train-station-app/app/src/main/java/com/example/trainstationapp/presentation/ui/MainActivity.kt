package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.ui.adapters.MainPagerViewAdapter
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var stationRepository: StationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.pager.adapter = MainPagerViewAdapter(this, stationRepository)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Stations"
                1 -> "About"
                else -> throw RuntimeException("No fragment here.")
            }
        }.attach()
    }
}
