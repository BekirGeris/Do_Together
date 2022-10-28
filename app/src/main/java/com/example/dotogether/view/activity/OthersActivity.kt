package com.example.dotogether.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityOthersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOthersBinding
    private lateinit var navController: NavController
    private var viewType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOthersBinding.inflate(layoutInflater)
        super.setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        navController = Navigation.findNavController(this, R.id.othersFragmentContainerView)

        viewType = intent.extras?.getInt("view_type")
        viewType?.let { startFragmentWithViewType(it) }
    }

    private fun startFragmentWithViewType(viewType: Int) {
        when(viewType) {
            0 -> {
                navController.navigate(R.id.shareFragment)
            }
            1 -> {
                navController.navigate(R.id.profileFragment)
            }
        }
    }
}