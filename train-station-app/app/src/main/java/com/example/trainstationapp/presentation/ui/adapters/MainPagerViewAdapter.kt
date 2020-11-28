package com.example.trainstationapp.presentation.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.ui.fragments.AboutFragment
import com.example.trainstationapp.presentation.ui.fragments.StationListFragment

class MainPagerViewAdapter(
    fragmentActivity: FragmentActivity,
    stationRepository: StationRepository,
) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        StationListFragment.newInstance(stationRepository),
        AboutFragment.newInstance()
    )

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}
