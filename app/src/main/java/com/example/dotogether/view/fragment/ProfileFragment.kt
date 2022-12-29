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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.FragmentProfileBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.util.Constants.MethodType
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.ProfileTargetAdapter
import com.example.dotogether.view.callback.HolderCallback
import com.example.dotogether.viewmodel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList
import kotlin.concurrent.thread

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), HolderCallback {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var itemProfileBinding: ItemProfileBinding

    private lateinit var imagePickerBuilder: ImagePicker.Builder

    private lateinit var targetAdapter: ProfileTargetAdapter
    private val targets = ArrayList<Target>()
    lateinit var user: User

    private var calledFromMode = -1

    private var justOneWork = true

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions  ->
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

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                when(calledFromMode) {
                    0 -> itemProfileBinding.backgroundImage.setImageURI(fileUri)
                    1 -> itemProfileBinding.profileImage.setImageURI(fileUri)
                }
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
            else -> {
                showToast("Task Cancelled")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
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
            initField()
            initObserve()
            justOneWork = false
        }
    }

    private fun initField() {

    }

    private fun initViews() {
        imagePickerBuilder = ImagePicker.with(this)

        user = User()

        targetAdapter = ProfileTargetAdapter(targets, User())

        targetAdapter.setOnClickListener(this)

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.targetRv.adapter = targetAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.data?.let { list ->
                        targets.clear()
                        targets.addAll(list)
                        targetAdapter.notifyDataSetChanged()
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }

        viewModel.getMyTargets()
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
        when(calledFromMode) {
            0 -> imagePickerBuilder.crop(2f, 1f)
            1 -> imagePickerBuilder.crop(1f, 1f)
        }
        imagePickerBuilder.compress(1024)
            .maxResultSize(1080, 1080)
            .saveDir(File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            .createIntent {
                resultLauncher.launch(it)
            }
    }

    override fun holderListener(binding: ViewBinding, methodType: MethodType, position: Int) {
        this.itemProfileBinding = binding as ItemProfileBinding
        when(methodType) {
            MethodType.METHOD_BACKGROUND_EDIT -> {
                calledFromMode = 0
                requestPermissionsForImagePicker()
            }
            MethodType.METHOD_PROFILE_EDIT -> {
                calledFromMode = 1
                requestPermissionsForImagePicker()
            }
            MethodType.METHOD_LOGOUT -> {
                dialog.shoe()
                viewModel.logout()
                thread {
                    Thread.sleep(1000)
                    activity?.finish()
                }
            }
            else -> {}
        }
    }
}