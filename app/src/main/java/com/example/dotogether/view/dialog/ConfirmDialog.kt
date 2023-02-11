package com.example.dotogether.view.dialog

import android.app.Activity
import android.app.Dialog
import com.example.dotogether.R
import com.example.dotogether.databinding.DialogConfirmBinding
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.example.dotogether.view.callback.ConfirmDialogListener

class ConfirmDialog(activity: Activity, private val confirmDialogListener: ConfirmDialogListener) {

    private var binding: DialogConfirmBinding
    var dialog: Dialog

    init {
        binding = DialogConfirmBinding.inflate(activity.layoutInflater)
        dialog = Dialog(activity)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setContentView(binding.root)

        binding.confirmBtn.setOnClickListener{
            confirmDialogListener.confirm()
            hide()
        }
        binding.cancelBtn.setOnClickListener{
            confirmDialogListener.cancel()
            hide()
        }
    }

    fun show() {
        dialog.tryShow()
    }

    fun hide() {
        dialog.dismiss()
    }
}