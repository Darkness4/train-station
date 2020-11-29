package com.example.trainstationapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.ui.adapters.MainPagerViewAdapter
import com.example.trainstationapp.presentation.ui.fragments.AboutFragment
import com.example.trainstationapp.presentation.ui.fragments.StationListFragment
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var stationRepository: StationRepository

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(stationRepository)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.pager.adapter = MainPagerViewAdapter(
            this,
            listOf(
                StationListFragment.newInstance(),
                AboutFragment.newInstance()
            )
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Stations"
                1 -> "About"
                else -> throw RuntimeException("No fragment here.")
            }
        }.attach()

        viewModel.showDetails.observe(this) {
            it?.let {
                startDetailsActivity(it)
                viewModel.showDetailsDone()
            }
        }
    }

    private fun startDetailsActivity(station: Station) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(STATION_MESSAGE, station)
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh_button -> {
            viewModel.refreshManuallyAndScrollToTop()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val STATION_MESSAGE = "com.example.trainstationapp.STATION_MESSAGE"
    }
}
