package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dotogether.R
import com.example.dotogether.databinding.FragmentLibraryBinding
import com.example.dotogether.view.adapter.TabsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : BaseFragment() {

    private lateinit var binding: FragmentLibraryBinding

    private lateinit var tabsPagerAdapter: TabsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLibraryBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        val numberOfTabs = 4
        binding.tabLayout.tabMode = TabLayout.MODE_FIXED
        binding.tabLayout.isInlineLabel = true

        tabsPagerAdapter = TabsPagerAdapter(childFragmentManager, lifecycle, numberOfTabs, binding.scrollView)
        binding.viewPager.adapter = tabsPagerAdapter

        // Enable Swipe
        binding.viewPager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.pending_targets)
                }
                1 -> {
                    tab.text = getString(R.string.subscriptions)
                }
                2 -> {
                    tab.text = getString(R.string.favorites)
                }
                3 -> {
                    tab.text = getString(R.string.completed)
                }
            }
        }.attach()
    }
}