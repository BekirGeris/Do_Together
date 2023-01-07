package com.example.dotogether.view.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dotogether.util.Constants
import com.example.dotogether.view.activity.OthersActivity
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

    fun goToProfileFragment(userId: Int) {
            val intent = Intent(requireContext(), OthersActivity::class.java)
            intent.putExtra("viewType", Constants.ViewType.VIEW_PROFILE_FRAGMENT.type)
            intent.putExtra("userId", userId)
            requireActivity().startActivity(intent)
    }

    fun goToChatFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_LIST_CHAT_FRAGMENT.type)
        startActivity(intent)
    }

    fun goToSearchFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_SEARCH_FRAGMENT.type)
        startActivity(intent)
    }
}