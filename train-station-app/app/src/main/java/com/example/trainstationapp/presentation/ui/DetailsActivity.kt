package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.ActivityDetailsBinding
import com.example.trainstationapp.domain.entities.Station
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.viewmodels.DetailsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var initialStation: Station
    private lateinit var viewModel: DetailsViewModel
    private lateinit var map: GoogleMap

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
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel.networkStatus.observe(this) {
            it?.doOnFailure { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.station.observe(this) {
            it?.let {
                // Add a marker in Sydney and move the camera
                val position = LatLng(it.fields!!.yWgs84, it.fields.xWgs84)
                map.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(it.libelle)
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
