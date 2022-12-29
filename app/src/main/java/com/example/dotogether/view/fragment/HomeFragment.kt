package com.example.dotogether.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.databinding.FragmentHomeBinding
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.HomeTargetAdapter
import com.example.dotogether.view.callback.HolderCallback
import com.example.dotogether.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : BaseFragment(), View.OnClickListener, HolderCallback {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    private lateinit var homeTargetAdapter: HomeTargetAdapter
    private val targets = ArrayList<Target>()
    private val reelsList = ArrayList<Reels>()

    private var justOneWork = true

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        var isGrantedGalleryAndCamera = true

        permissions.forEach { actionMap ->
            when (actionMap.key) {
                Manifest.permission.READ_EXTERNAL_STORAGE ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
                Manifest.permission.CAMERA ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
            }
        }

        if (isGrantedGalleryAndCamera) {
            startImageMaker()
        } else {
            showToast("Gallery and camera permission must be granted!")
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!
            //todo: test amaçlı eklendi silinecek
            binding.image.setImageURI(fileUri)
            binding.image.visibility = View.VISIBLE

            Thread {
                Thread.sleep(3000)
                activity?.runOnUiThread {
                    binding.image.visibility = View.GONE
                }
            }.start()

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast("Task Cancelled")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        justOneWork = true
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
        if (justOneWork) {
            initObserve()
            justOneWork = false
        }
    }

    fun initViews() {
        binding.cameraBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
        binding.searchBtn.setOnClickListener(this)

        navController = findNavController()

        homeTargetAdapter = HomeTargetAdapter(targets, reelsList)
        homeTargetAdapter.setOnClickListener(this)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = homeTargetAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.allTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.data?.let { list ->
                        if (list.isEmpty()) {
                            binding.activityErrorView.visibility = View.VISIBLE
                        }
                        targets.clear()
                        targets.addAll(list)
                        targets.reverse()
                        homeTargetAdapter.notifyDataSetChanged()
                        //todo: **************test test****************
                        for (i in 1..100) {
                            reelsList.add(Reels())
                        }
                        if (reelsList.isEmpty()) {
                            homeTargetAdapter.reelsTopHolder?.binding?.reelsRv?.visibility = View.GONE
                        }
                        homeTargetAdapter.reelsTopHolder?.reelsAdapter?.notifyDataSetChanged()
                        //todo: **************test test****************
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    binding.activityErrorView.visibility = View.VISIBLE
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        viewModel.getAllTargets()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.cameraBtn -> {
                requestPermissionsForImagePicker()
            }
            binding.messageBtn -> {
                navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_LIST_CHAT_FRAGMENT))
            }
            binding.searchBtn -> {
                //navController.navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSearch())
            }
        }
    }

    fun requestPermissionsForImagePicker() {
        PermissionUtil.requestPermissions(
            requireContext(),
            requireActivity(),
            binding.root,
            requestMultiplePermissions,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ) {
            startImageMaker()
        }
    }

    private fun startImageMaker() {
        ImagePicker.with(this)
            .crop(1f, 1f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .saveDir(File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            .createIntent {
                resultLauncher.launch(it)
            }
    }

    override fun holderListener(binding: ViewBinding, methodType: Constants.MethodType, position: Int) {
        when(binding) {
            is ItemReelsTopBinding -> {
                reelsTopHolderClick(binding, methodType, position)
            }
            is ItemTargetBinding -> {
                targetHolderClick(binding, methodType, position)
            }
        }
    }

    private fun reelsTopHolderClick(binding: ItemReelsTopBinding, methodType: Constants.MethodType, position: Int) {
        when(methodType) {
            Constants.MethodType.METHOD_REELS -> {

            }
            else -> {}
        }
    }

    private fun targetHolderClick(binding: ItemTargetBinding, methodType: Constants.MethodType, position: Int) {
        when(methodType) {
            Constants.MethodType.METHOD_LIKE_TARGET -> {
                viewModel.likeTarget.observe(viewLifecycleOwner) {
                    when(it) {
                        is Resource.Success -> {
                            dialog.hide()
                            viewModel.getAllTargets()
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
                viewModel.likeTarget(targets[position].id!!)
            }
            Constants.MethodType.METHOD_JOIN_TARGET -> {
                viewModel.joinTarget.observe(viewLifecycleOwner) {
                    when(it) {
                        is Resource.Success -> {
                            dialog.hide()
                            viewModel.getAllTargets()
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
                viewModel.joinTarget(targets[position].id!!)
            }
            else -> {}
        }
    }
}