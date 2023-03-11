package com.example.dotogether.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotogether.databinding.FragmentLibraryBinding
import com.example.dotogether.view.adapter.TabsPagerAdapter
import com.example.dotogether.viewmodel.LibraryViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LibraryFragment : BaseFragment() {

    private val viewModel: LibraryViewModel by viewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
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
                    tab.text = "Pending Targets"
                }
                1 -> {
                    tab.text = "Subscriptions"
                }
                2 -> {
                    tab.text = "Favorites"
                }
                3 -> {
                    tab.text = "Completed"
                }
            }
        }.attach()
    }

    private fun initObserve() {

    }
}