package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.presentation.ui.adapters.MainPagerViewAdapter
import com.example.trainstationapp.presentation.ui.fragments.AboutFragment
import com.example.trainstationapp.presentation.ui.fragments.StationListFragment
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var stationListFragmentFactory: StationListFragment.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.pager.adapter = MainPagerViewAdapter(
            this,
            listOf(
                stationListFragmentFactory.newInstance(),
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
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.refresh_button -> {
            viewModel.refreshManually()
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
}
