package com.example.dotogether.view.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dotogether.view.dialog.CustomProgressDialog

open class BaseFragment : Fragment() {

    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireActivity())
    }

    fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}