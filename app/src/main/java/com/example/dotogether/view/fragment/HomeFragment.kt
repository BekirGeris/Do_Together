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
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.databinding.FragmentHomeBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.HomeTargetAdapter
import com.example.dotogether.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : BaseFragment(), View.OnClickListener {

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
}