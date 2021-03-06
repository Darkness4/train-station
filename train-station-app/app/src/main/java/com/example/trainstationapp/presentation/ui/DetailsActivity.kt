package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.trainstationapp.R
import com.example.trainstationapp.core.state.doOnFailure
import com.example.trainstationapp.databinding.ActivityDetailsBinding
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.viewmodels.DetailsViewModel
import com.example.trainstationapp.presentation.viewmodels.DetailsViewModel.Companion.provideFactory
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
    private companion object {
        const val DEFAULT_ZOOM_LEVEL = 15.0f
    }

    private lateinit var binding: ActivityDetailsBinding
    private val args: DetailsActivityArgs by navArgs()

    @Inject
    lateinit var assisted: DetailsViewModel.AssistedFactory
    private val viewModel by viewModels<DetailsViewModel> {
        assisted.provideFactory(args.station)
    }
    private lateinit var map: GoogleMap

    @Inject
    lateinit var stationRepository: StationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Watch for errors
        viewModel.networkStatus.observe(this) {
            it?.doOnFailure { e ->
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        viewModel.station.observe(this) {
            it?.let {
                val position = LatLng(it.fields!!.yWgs84, it.fields.xWgs84)
                map.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(it.libelle)
                )
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, DEFAULT_ZOOM_LEVEL))
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
}
