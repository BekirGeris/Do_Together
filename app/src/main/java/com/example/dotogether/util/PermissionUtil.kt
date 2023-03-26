package com.example.dotogether.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dotogether.R
import com.google.android.material.snackbar.Snackbar

object PermissionUtil {

    fun requestPermissions(
        context: Context,
        activity: Activity,
        view: View,
        requestMultiplePermissions: ActivityResultLauncher<Array<String>>,
        vararg permissions: String,
        allPermissionGranted: () -> Unit) {

        if (isPermissionsGranted(context, *permissions)) {
            //all permission granted
            allPermissionGranted()
        } else {
            //all permission not granted
            if (isAgainRequestPermission(activity, *permissions)) {
                Snackbar.make(view,
                    context.getString(R.string.permission_needed_feature),
                    Snackbar.LENGTH_INDEFINITE).setAction(context.getString(R.string.give_permission)) {
                    //again request permission
                    requestMultiplePermissions.launch(
                        permissions.toList().toTypedArray()
                    )
                }.show()
            } else {
                requestMultiplePermissions.launch(
                    permissions.toList().toTypedArray()
                )
            }
        }
    }

    private fun isPermissionsGranted(context: Context, vararg permissions: String) : Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun isAgainRequestPermission(activity: Activity, vararg permissions: String) : Boolean {
        permissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                return true
            }
        }
        return false
    }
}