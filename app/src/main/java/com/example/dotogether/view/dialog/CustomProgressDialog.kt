package com.example.dotogether.view.dialog

import android.app.Activity
import android.app.Dialog
import com.example.dotogether.R
import com.example.dotogether.databinding.CustomProgressDialogBinding
import com.example.dotogether.util.helper.RuntimeHelper.tryShow

class CustomProgressDialog(activity: Activity) {

    private var binding: CustomProgressDialogBinding
    var dialog: Dialog

    init {
        val inflater = activity.layoutInflater
        binding = CustomProgressDialogBinding.inflate(inflater)
        dialog = Dialog(activity)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
    }

    fun shoe() {
        dialog.tryShow()
    }

    fun hide() {
        dialog.dismiss()
    }
}