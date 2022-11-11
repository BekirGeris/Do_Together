package com.example.dotogether.view.fragment

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentProfileBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.view.adapter.ProfileTargetAdapter
import com.example.dotogether.view.callback.HolderCallback
import com.example.dotogether.viewmodel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList

@AndroidEntryPoint
class ProfileFragment : Fragment(), HolderCallback {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    private lateinit var targetAdapter: ProfileTargetAdapter
    private val targets = ArrayList<Target>()
    lateinit var user: User

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
            Toast.makeText(requireContext(), "Gallery and camera permission must be granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data!!
            println("bekbek $fileUri")

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
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

    private fun initViews() {
        for (i in 1..1000) {
            targets.add(Target())
        }

        user = User()

        targetAdapter = ProfileTargetAdapter(targets, User())

        targetAdapter.setOnClickListener(this)

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.targetRv.adapter = targetAdapter

        println("bekbek ${viewModel.text.value}")
    }

    fun requestPermissions() {
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

    override fun holderListener(methodType: Int, itemId: Int) {
        when(methodType) {
            5 -> requestPermissions()
            else -> println("else")
        }
    }
}