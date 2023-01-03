package com.example.dotogether.view.adapter.holder

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TargetHolder(
    view: View,
    val layoutInflater: LayoutInflater,
    private val listener: HolderListener.TargetHolderListener
) : BaseHolder(view), View.OnClickListener {

    private lateinit var target: Target
    private val binding = ItemTargetBinding.bind(view)
    private val context = binding.root.context

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    private lateinit var memberAdapter: MemberAdapter

    init {
        initViews()
    }

    private fun initViews() {
        binding.membersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.holderView.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.userImage.setOnClickListener(this)
        binding.postUserName.setOnClickListener(this)
        binding.postTime.setOnClickListener(this)
        binding.postImage.setOnClickListener(this)
        binding.like.setOnClickListener(this)
        binding.join.setOnClickListener(this)

        dialogBinding.save.visibility = View.VISIBLE
        dialogBinding.share.visibility = View.VISIBLE
        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.save.setOnClickListener(this)
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.delete.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun bind(target: Target) {
        this.target = target
        val user = target.admin
        binding.targetTitle.text = target.target
        binding.description.text = target.description
        target.created_at?.let {
            val date = Constants.DATE_FORMAT_3.parse(it)
            binding.postTime.text = date?.let { it1 -> Constants.DATE_FORMAT_2.format(it1) }
        }

        target.is_joined?.let {
            if (it) {
                binding.join.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_add_circle_24), null, null, null)
                binding.join.text = context.getString(R.string.leave)
            } else {
                binding.join.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_add_circle_outline_24), null, null, null)
                binding.join.text = context.getString(R.string.join)
            }
        }

        target.is_liked?.let {
            if (it) {
                binding.like.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_thumb_up_alt_24), null, null, null)
                binding.like.text = context.getString(R.string.un_like)
            } else {
                binding.like.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_thumb_up_off_alt_24), null, null, null)
                binding.like.text = context.getString(R.string.like)
            }
        }

        target.users?.let {
            memberAdapter = MemberAdapter(it, false)
            binding.membersRv.adapter = memberAdapter
        }

        user?.let {
            binding.postUserName.text = it.username
            RuntimeHelper.glideForPersonImage(context).load(it.img).into(binding.userImage)
        }

        if (target.img != null) {
            RuntimeHelper.glideForImage(context).load(target.img).into(binding.postImage)
        } else {
            binding.postImage.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (v) {
            binding.holderView, binding.postImage -> {
                goToTargetFragment(navController, target.id!!)
            }
            binding.moreSettingBtn -> {
                bottomSheetDialog.show()
            }
            binding.postUserName, binding.userImage, binding.postTime -> {
                target.admin?.id?.let { goToProfileFragment(navController, it) }
            }
            binding.like -> {
                if (target.is_liked == false)
                    listener.like(binding, target)
                else
                    listener.unLike(binding, target)
            }
            binding.join -> {
                if (target.is_joined == false)
                    listener.join(binding, target)
                else
                    listener.unJoin(binding, target)
            }
            dialogBinding.save -> {
                bottomSheetDialog.hide()
            }
            dialogBinding.share -> {
                bottomSheetDialog.hide()
            }
            dialogBinding.delete -> {
                bottomSheetDialog.hide()
                listener.deleteTarget(binding, target)
            }
        }
    }
}