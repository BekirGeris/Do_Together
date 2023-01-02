package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentReelsBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.ReelsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

@AndroidEntryPoint
class ReelsFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ReelsViewModel by viewModels()
    private lateinit var binding: FragmentReelsBinding

    private var justOneWork = true
    var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReelsBinding.inflate(layoutInflater)

        initViews();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (justOneWork) {
            initObserve()
            justOneWork = false
        }
    }

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)
        binding.reelsUserName.setOnClickListener(this)
        binding.reelsImage.setOnClickListener(this)

        userId = arguments?.getInt("userId", -1)
    }

    private fun initObserve() {
        viewModel.reels.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        showReels(it)
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        viewModel.getFollowingsReels()
    }

    private fun showReels(users: ArrayList<User>) {
        try {
            thread {
                users.forEach {
                    activity?.runOnUiThread { binding.reelsUserName.text = it.username }

                    it.active_statuses?.forEach { reels ->
                        activity?.runOnUiThread { RuntimeHelper.glideForImage(requireContext()).load(reels.img).into(binding.reelsImage) }
                        Thread.sleep(3000)
                    }
                }
                activity?.finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
       navController?.let {
           when(v) {
               binding.backBtn -> {
                   activity?.onBackPressed()
               }
               binding.reelsUserName -> {
                    navController.navigate(ReelsFragmentDirections.actionReelsFragmentToProfileFragment())
               }
               binding.reelsImage -> {

               }
               else -> {}
           }
       }
    }
}