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
        val numberOfTabs = 3
        binding.tabLayout.tabMode = TabLayout.MODE_FIXED
        binding.tabLayout.isInlineLabel = true

        val adapter = TabsPagerAdapter(childFragmentManager, lifecycle, numberOfTabs, binding.scrollView)
        binding.viewPager.adapter = adapter

        // Enable Swipe
        binding.viewPager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Subscriptions"
                    //tab.setIcon(R.drawable.ic_music)
                }
                1 -> {
                    tab.text = "Favorites"
                    //tab.setIcon(R.drawable.ic_movie)

                }
                2 -> {
                    tab.text = "Completed"
                    //tab.setIcon(R.drawable.ic_book)
                }
            }
//             Change color of the icons
//            tab.icon?.colorFilter =
//                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
//                    Color.WHITE,
//                    BlendModeCompat.SRC_ATOP
//                )
        }.attach()
    }
}