package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.NavHostFragment
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.presentation.ui.adapters.MainPagerViewAdapter
import com.example.trainstationapp.presentation.ui.fragments.AboutFragment
import com.example.trainstationapp.presentation.ui.theme.MyApplicationTheme
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel>()

    private val fragments by lazy {
        listOf(
            NavHostFragment.create(R.navigation.station_list_nav_graph),
            AboutFragment.newInstance()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
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
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.refresh_button -> {
                viewModel.refreshManuallyAndScrollToTop()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}