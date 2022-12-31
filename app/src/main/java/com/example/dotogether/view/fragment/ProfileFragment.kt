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
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.FragmentProfileBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.ProfileTargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.ArrayList
import kotlin.concurrent.thread

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), HolderListener.ProfileHolderListener, HolderListener.TargetHolderListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var itemProfileBinding: ItemProfileBinding

    private lateinit var imagePickerBuilder: ImagePicker.Builder

    private lateinit var targetAdapter: ProfileTargetAdapter
    private val targets = ArrayList<Target>()
    private var myUser: User? = null

    var userId: Int? = null

    private var calledFromMode = -1

    private var justOneWork = true

    private var nextPage = "2"
    private lateinit var scrollListener: RecyclerView.OnScrollListener

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

        userId = arguments?.getInt("userId", -1)

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)

        binding.swipeLyt.setOnRefreshListener {
            //viewModel.getMyTargets()
            userId?.let {
                viewModel.getUser(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myUser.observe(viewLifecycleOwner) { user ->
            myUser = user
            userId = myUser?.id
            user.id?.let { viewModel.getUser(it) }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                    it.data?.let { user ->
                        user.activities?.let { list ->
                            targets.clear()
                            targets.addAll(list)
                            targetAdapter = ProfileTargetAdapter(targets, user, this, this)
                            binding.targetRv.adapter = targetAdapter
                        }
                    }
                }
                is Resource.Error -> {
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                }
                is Resource.Loading -> {
                    if (!binding.swipeLyt.isRefreshing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        if (userId != null) {
            viewModel.getUser(userId!!)
        } else {
            viewModel.getMyUser()
        }
//        viewModel.myTargets.observe(viewLifecycleOwner) {
//            when(it) {
//                is Resource.Success -> {
//                    binding.swipeLyt.isRefreshing = false
//                    it.data?.let { response ->
//                        response.data?.let { list ->
//                            //binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
//                            targets.clear()
//                            targets.addAll(list)
//                            targetAdapter.notifyDataSetChanged()
//                        }
//                        response.next_page_url?.let { next_page_url ->
//                            nextPage = next_page_url.last().toString()
//                            setRecyclerViewScrollListener()
//                        }
//                    }
//                    dialog.hide()
//                }
//                is Resource.Error -> {
//                    //binding.activityErrorView.visibility = View.VISIBLE
//                    binding.swipeLyt.isRefreshing = false
//                    dialog.hide()
//                    showToast(it.message)
//                }
//                is Resource.Loading -> {
//                    if (!binding.swipeLyt.isRefreshing) {
//                        dialog.shoe()
//                    }
//                }
//                else -> {}
//            }
//        }
//        viewModel.nextMyTargets.observe(viewLifecycleOwner) {
//            when(it) {
//                is Resource.Success -> {
//                    it.data?.let { response ->
//                        response.data?.let { list ->
//                            targets.addAll(list)
//                            targetAdapter.notifyDataSetChanged()
//                        }
//                        response.next_page_url?.let { next_page_url ->
//                            nextPage = next_page_url.last().toString()
//                            setRecyclerViewScrollListener()
//                        }
//                    }
//                }
//                is Resource.Error -> {
//                }
//                is Resource.Loading -> {
//                }
//                else -> {}
//            }
//        }
        viewModel.likeJoinLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { updateTarget ->
                        val newTargets = ArrayList<Target>()
                        targets.mapTo(newTargets) {t -> if (updateTarget.id == t.id) updateTarget else t}
                        targets.clear()
                        targets.addAll(newTargets)
                        targetAdapter.notifyDataSetChanged()
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
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1)) {
                    viewModel.getNextMyTargets(nextPage)
                    binding.targetRv.removeOnScrollListener(scrollListener)
                }
            }
        }
        binding.targetRv.addOnScrollListener(scrollListener)
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

    override fun itIsMe(binding: ItemProfileBinding, user: User): Boolean {
        return user.id == myUser?.id
    }

    override fun backgroundImageEdit(binding: ItemProfileBinding, user: User) {
        calledFromMode = 0
        requestPermissionsForImagePicker()
    }

    override fun profileImageEdit(binding: ItemProfileBinding, user: User) {
        calledFromMode = 1
        requestPermissionsForImagePicker()
    }

    override fun finishActivity(binding: ItemProfileBinding, user: User) {
        requireActivity().finish()
    }

    override fun logout(binding: ItemProfileBinding, user: User) {
        dialog.shoe()
        viewModel.logout()
        thread {
            Thread.sleep(1000)
            activity?.finish()
        }
    }

    override fun like(binding: ItemTargetBinding, target: Target) {
        viewModel.likeTarget(target.id!!)
    }

    override fun join(binding: ItemTargetBinding, target: Target) {
        viewModel.joinTarget(target.id!!)
    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {
        viewModel.unLikeTarget(target.id!!)
    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {
        viewModel.unJoinTarget(target.id!!)
    }
}