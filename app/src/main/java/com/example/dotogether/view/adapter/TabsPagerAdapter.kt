package com.example.dotogether.view.adapter

import android.os.Bundle
import android.widget.HorizontalScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dotogether.view.fragment.SubscriptionsFragment
import com.example.dotogether.view.fragment.FavoritesFragment
import com.example.dotogether.view.fragment.CompletedFragment
import kotlin.concurrent.thread

class TabsPagerAdapter(fm: FragmentManager,lifecycle: Lifecycle, private var numberOfTabs: Int, val scrollView: HorizontalScrollView) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("fragmentName", "SubscriptionsFragment Fragment")
                val subscriptionsFragment = SubscriptionsFragment()
                subscriptionsFragment.arguments = bundle
                return subscriptionsFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("fragmentName", "FavoritesFragment Fragment")
                val favoritesFragment = FavoritesFragment()
                favoritesFragment.arguments = bundle
                return favoritesFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("fragmentName", "CompletedFragment Fragment")
                val completedFragment = CompletedFragment()
                completedFragment.arguments = bundle
                return completedFragment
            }
            else -> return SubscriptionsFragment()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    var b = true

    override fun getItemId(position: Int): Long {
        if (b) {
            b = false
            thread {
                Thread.sleep(500)
                scrollView.smoothScrollTo(position * 200, 0)
                b = true
            }
        }
        return super.getItemId(position)
    }

}