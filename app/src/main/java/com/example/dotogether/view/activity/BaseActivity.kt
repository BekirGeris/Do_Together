package com.example.dotogether.view.activity

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.view.dialog.ConfirmDialog

abstract class BaseActivity : AppCompatActivity() {

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun showAlertDialog(message: String, listener: ConfirmDialogListener) {
        ConfirmDialog(this, message, listener).show()
    }

    fun goToLoginFragment() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}