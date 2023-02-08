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
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentHomeBinding
import com.example.dotogether.databinding.ItemReelsBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Basket
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.CreateReelsRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.example.dotogether.view.adapter.HomeTargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import omari.hamza.storyview.callback.StoryClickListeners
import java.io.File

@AndroidEntryPoint
class HomeFragment : BaseFragment(), View.OnClickListener, HolderListener.TargetHolderListener, HolderListener.ReelsHolderListener, HolderListener.ReelsTopHolderListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeTargetAdapter: HomeTargetAdapter
    private val targets = ArrayList<Target>()
    private val reelsList = ArrayList<User>()

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getNextAllTargets(nextPage)
                binding.targetRv.removeOnScrollListener(this)
            }
        }
    }

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

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                createReels(fileUri.toString())
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
            else -> {
                showToast("Task Cancelled")
            }
        }
    }

    private fun createReels(fileUri: String) {
        val filePath = fileUri.replace("file://", "")

        try {
            val createReelsRequest = CreateReelsRequest(RuntimeHelper.imageToBase64(filePath))
            viewModel.createReels(createReelsRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(dialogBinding.root.context, R.style.BottomSheetDialogTheme)

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
    }

    fun initViews() {
        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.notificationBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
        binding.searchBtn.setOnClickListener(this)

        dialogBinding.createReels.visibility = View.VISIBLE
        dialogBinding.createTarget.visibility = View.VISIBLE
        dialogBinding.createReels.setOnClickListener(this)
        dialogBinding.createTarget.setOnClickListener(this)

        homeTargetAdapter = HomeTargetAdapter(targets, reelsList, this, this, this)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = homeTargetAdapter

        binding.swipeLyt.setOnRefreshListener {
            viewModel.getFollowingsReels()
            viewModel.getAllTargets()
            viewModel.getMyUserFromRemote()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyUserFromRemote()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myUserRemote.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    setTotalUnreadCount(it.data?.unread_count ?: 0)
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {}
                else -> {}
            }
        }
        viewModel.reels.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { list ->
                        reelsList.clear()
                        reelsList.addAll(list)
                    }
                    homeTargetAdapter.reelsTopHolder?.reelsAdapter?.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    reelsList.clear()
                    homeTargetAdapter.reelsTopHolder?.reelsAdapter?.notifyDataSetChanged()
                }
                is Resource.Loading -> {
                }
                else -> {}
            }
        }
        viewModel.getFollowingsReels()
        viewModel.updateTarget.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { updateTarget ->
                        val newTargets = ArrayList<Target>()
                        targets.mapTo(newTargets) { t -> if (updateTarget.id == t.id) updateTarget else t }
                        targets.clear()
                        targets.addAll(newTargets)
                        homeTargetAdapter.notifyDataSetChanged()
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    showToast(it.message)
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        viewModel.allTargets.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    dialog.hide()
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                            targets.clear()
                            targets.addAll(list)
                            homeTargetAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                }
                is Resource.Error -> {
                    binding.swipeLyt.isRefreshing = false
                    binding.activityErrorView.visibility = View.VISIBLE
                    dialog.hide()
                    showToast(it.message)
                    targets.clear()
                    reelsList.clear()
                    homeTargetAdapter.reelsTopHolder?.reelsAdapter?.notifyDataSetChanged()
                    homeTargetAdapter.notifyDataSetChanged()
                }
                is Resource.Loading -> {
                    if (!binding.swipeLyt.isRefreshing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.getAllTargets()
        viewModel.nextAllTargets.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { response ->
                        response.data?.let { list ->
                            targets.addAll(list)
                            homeTargetAdapter.notifyDataSetChanged()
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
        viewModel.createReels.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    viewModel.getFollowingsReels()
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
        viewModel.getCurrentBasket().observe(viewLifecycleOwner) {basket ->
            basket?.let {
                Log.d(TAG, "home fragment basket: $it")
                if (it.refreshType == Constants.CREATE_TARGET) {
                    viewModel.getAllTargets()
                } else if (it.refreshType == Constants.CHAT) {
                    viewModel.getMyUserFromRemote()
                }
            }
        }
    }

    private fun setTotalUnreadCount(totalUnreadCount: Int) {
        if (totalUnreadCount != 0) {
            binding.unreadCount.text = totalUnreadCount.toString()
            binding.unreadCountLyt.visibility = View.VISIBLE
        } else {
            binding.unreadCountLyt.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.notificationBtn -> {
                goToNotificationFragment()
            }
            binding.messageBtn -> {
                goToChatListFragment()
            }
            binding.searchBtn -> {
                goToSearchFragment()
            }
            dialogBinding.createReels -> {
                bottomSheetDialog.hide()
                requestPermissionsForImagePicker()
            }
            dialogBinding.createTarget -> {
                bottomSheetDialog.hide()
                goToShareFragment()
            }
        }
    }

    fun actionOnClick(clickType: Int) {
        when (clickType) {
            1 -> {
                bottomSheetDialog.tryShow()
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
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .saveDir(File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            .createIntent {
                resultLauncher.launch(it)
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

    override fun onClickReels(binding: ItemReelsBinding, user: User) {
        showReels(user, false, object : StoryClickListeners {
            override fun onDescriptionClickListener(position: Int) {

            }

            override fun onTitleIconClickListener(position: Int) {
                user.id?.let { goToProfileFragment(it) }
                reelsViewBuilder.dismiss()
            }

            override fun onDeleteIconClickListener(p0: Int) {

            }
        })
    }

    override fun addReels() {
        requestPermissionsForImagePicker()
    }

    override fun goToRecyclerViewTop() {
        binding.targetRv.smoothScrollToPosition(0)
    }

    override fun onStop() {
        super.onStop()
        val basket = viewModel.getCurrentBasketSync() ?: Basket()
        basket.refreshType = Constants.NONE
        viewModel.updateBasket(basket)
    }
}