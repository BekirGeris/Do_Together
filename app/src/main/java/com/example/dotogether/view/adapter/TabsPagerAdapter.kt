package com.example.dotogether.view.adapter

import android.os.Bundle
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dotogether.view.fragment.*
import kotlin.concurrent.thread

class TabsPagerAdapter(fm: FragmentManager,lifecycle: Lifecycle, private var numberOfTabs: Int, private val scrollView: HorizontalScrollView) : FragmentStateAdapter(fm, lifecycle) {

    lateinit var activeFragment: BaseFragment

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        when (position) {
            0 -> {
                bundle.putString("fragmentName", "PendingTargetsFragment Fragment")
                activeFragment = PendingTargetsFragment()
                activeFragment.arguments = bundle
            }
            1 -> {
                bundle.putString("fragmentName", "SubscriptionsFragment Fragment")
                activeFragment = SubscriptionsFragment()
                activeFragment.arguments = bundle
            }
            2 -> {
                bundle.putString("fragmentName", "FavoritesFragment Fragment")
                activeFragment = FavoritesFragment()
                activeFragment.arguments = bundle
            }
            3 -> {
                bundle.putString("fragmentName", "CompletedFragment Fragment")
                activeFragment = CompletedFragment()
                activeFragment.arguments = bundle
            }
            else -> {
                activeFragment = SubscriptionsFragment()
            }
        }
        return activeFragment
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    var b = true
    var p = 0
    override fun getItemId(position: Int): Long {
        p = position
        if (b) {
            b = false
            thread {
                Thread.sleep(500)
                scrollView.smoothScrollTo(p * 200, 0)
                b = true
            }
        }
        return super.getItemId(position)
    }
}