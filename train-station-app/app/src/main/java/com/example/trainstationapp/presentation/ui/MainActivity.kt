package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.presentation.ui.adapters.MainPagerViewAdapter
import com.example.trainstationapp.presentation.ui.fragments.AboutFragment
import com.example.trainstationapp.presentation.viewmodels.AuthViewModel
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val auth = FirebaseAuth.getInstance()

    private val viewModel by viewModels<MainViewModel>()
    private val authViewModel by viewModels<AuthViewModel> { AuthViewModel.provideFactory(auth) }

    private val fragments by lazy {
        listOf(
            NavHostFragment.create(R.navigation.station_list_nav_graph),
            AboutFragment.newInstance()
        )
    }

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res -> println(res) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.authViewModel = authViewModel
        binding.lifecycleOwner = this

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Add the fragments to the PagerView
        binding.pager.adapter = MainPagerViewAdapter(this, fragments)

        // Link the TabLayout and the PagerView
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text =
                when (position) {
                    0 -> "Stations"
                    1 -> "About"
                    else -> throw RuntimeException("No fragment here.")
                }
        }
            .attach()

        if (auth.currentUser == null) {
            createSignInIntent()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.refresh_button -> {
                viewModel.refreshManuallyAndScrollToTop()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers =
            arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
            )

        // Create and launch sign-in intent
        val signInIntent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signInIntent)
    }
}
