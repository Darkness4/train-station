package com.example.trainstationapp.presentation.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter for a `ViewPager2`.
 *
 * Show the fragment on the `ViewPager2`.
 */
class MainPagerViewAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>,
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}
