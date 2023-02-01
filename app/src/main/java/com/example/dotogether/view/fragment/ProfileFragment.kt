package com.example.dotogether.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.view.activity.OthersActivity
import com.example.dotogether.view.adapter.ProfileTargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.view.dialog.ConfirmDialog
import com.example.dotogether.viewmodel.ProfileViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import omari.hamza.storyview.callback.StoryClickListeners
import java.io.File
import kotlin.concurrent.thread

@AndroidEntryPoint
class ProfileFragment : BaseFragment(), HolderListener.ProfileHolderListener, HolderListener.TargetHolderListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var itemProfileBinding: ItemProfileBinding

    private lateinit var imagePickerBuilder: ImagePicker.Builder

    private lateinit var targetAdapter: ProfileTargetAdapter
    private val targets = ArrayList<Target>()

    private var itIsMe: Boolean = false
    var userId: Int? = null

    private var calledFromMode = -1

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1)) {
                userId?.let { viewModel.getNextTargetsWithUserId(it, nextPage) }
                binding.targetRv.removeOnScrollListener(this)
            }
        }
    }

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
                    0 -> changeBackgroundImage(fileUri.toString())
                    1 -> changeProfileImage(fileUri.toString())
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
        initObserve()
        sendFirebaseToken()
    }

    private fun initViews() {
        imagePickerBuilder = ImagePicker.with(this)

        userId = arguments?.getInt("userId", -1)

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)

        binding.swipeLyt.setOnRefreshListener {
            userId?.let { viewModel.getUser(it) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myUser.observe(viewLifecycleOwner) { user ->
            if (userId == null || user.id == userId) {
                userId = user.id
                itIsMe = true
                user.id?.let { viewModel.getUser(it) }
            } else {
                itIsMe = false
                viewModel.getUser(userId!!)
            }
        }
        viewModel.getMyUser()
        viewModel.user.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { user ->
                        targetAdapter = ProfileTargetAdapter(targets, user, this, this)
                        binding.targetRv.adapter = targetAdapter
                    }
                    userId?.let { it1 -> viewModel.getTargetsWithUserId(it1) }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.targets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            //binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                            targets.clear()
                            targets.addAll(list)
                            targetAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    //binding.activityErrorView.visibility = View.VISIBLE
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.nextTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { response ->
                        response.data?.let { list ->
                            targets.addAll(list)
                            targetAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                else -> {}
            }
        }
        viewModel.updateTarget.observe(viewLifecycleOwner) {
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
        viewModel.updateUser.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    userId?.let { viewModel.getUser(it) }
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
        viewModel.deleteTarget.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { updateTarget ->
                        val newTargets = ArrayList<Target>()
                        targets.filter { updateTarget.id != it.id }.mapTo(newTargets) { it }
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
        viewModel.removeReels.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    viewModel.getMyUser()
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

    private fun changeBackgroundImage(fileUri: String) {
        val updateUserRequest = UpdateUserRequest()
        val filePath = fileUri.replace("file://", "")

        try {
            updateUserRequest.background_img = RuntimeHelper.imageToBase64(filePath)
            viewModel.updateUser(updateUserRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun changeProfileImage(fileUri: String) {
        val updateUserRequest = UpdateUserRequest()
        val filePath = fileUri.replace("file://", "")

        try {
            updateUserRequest.img = RuntimeHelper.imageToBase64(filePath)
            viewModel.updateUser(updateUserRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun isMe(binding: ItemProfileBinding, user: User): Boolean {
        return itIsMe
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
            goToLoginFragment()
            activity?.finish()
        }
    }

    override fun isOtherActivity() : Boolean {
        return requireActivity() is OthersActivity
    }

    override fun follow(binding: ItemProfileBinding, user: User) {
        user.id?.let { viewModel.follow(it) }
    }

    override fun unFollow(binding: ItemProfileBinding, user: User) {
        user.id?.let { viewModel.unFollow(it) }
    }

    override fun showReels(binding: ItemProfileBinding, user: User) {
        showReels(user, itIsMe, object : StoryClickListeners {
            override fun onDescriptionClickListener(position: Int) {

            }

            override fun onTitleIconClickListener(position: Int) {
                reelsViewBuilder.dismiss()
            }

            override fun onDeleteIconClickListener(p0: Int) {
                reelsViewBuilder.pauseStories()

                ConfirmDialog(requireActivity(), object : ConfirmDialogListener {
                    override fun cancel() {
                        reelsViewBuilder.startStories()
                    }

                    override fun confirm() {
                        reelsViewBuilder.dismiss()
                        user.active_statuses?.get(p0)?.let { viewModel.removeReels(it.id) }
                    }
                }).shoe()
            }
        })
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

    override fun deleteTarget(binding: ItemTargetBinding, target: Target) {
        super.deleteTarget(binding, target)
        target.id?.let { viewModel.deleteTarget(it) }
    }

    private fun sendFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            //Log.d(TAG, "token : ${task.result}")
            if (SharedPreferencesUtil.getString(requireContext(), Constants.FIREBASE_TOKEN, "") != task.result) {
                SharedPreferencesUtil.setString(requireContext(), Constants.FIREBASE_TOKEN, task.result)
                val updateUserRequest = UpdateUserRequest()
                updateUserRequest.fcm_token = task.result
                viewModel.updateUser(updateUserRequest)
            }
        })
    }
}