package com.example.dotogether.view.fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dotogether.model.User
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryParse
import com.example.dotogether.view.activity.LoginActivity
import com.example.dotogether.view.activity.OthersActivity
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.view.dialog.ConfirmDialog
import com.example.dotogether.view.dialog.CustomProgressDialog
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory

open class BaseFragment : Fragment() {

    lateinit var dialog: CustomProgressDialog
    lateinit var reelsViewBuilder: StoryView.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = CustomProgressDialog(requireActivity())
    }

    fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showReels(user: User, isDeleteIconVisibility: Boolean, reelsClickListener: StoryClickListeners) {
        val stories: ArrayList<MyStory> = ArrayList()

        user.active_statuses?.forEach {
            val date = Constants.DATE_FORMAT_3.tryParse(it.created_at)

            stories.add(
                MyStory(
                    it.img,
                    date
                )
            )
        }

        reelsViewBuilder = StoryView.Builder(parentFragmentManager)
            .setStoriesList(stories)
            .setStoryDuration(5000)
            .setTitleLogoUrl(user.img)
            .setTitleText(user.username)
            .setStoryClickListeners(reelsClickListener)
            .setStartingIndex(0)
            .setRequestManager(RuntimeHelper.glideForImage(requireContext()))
            .setDeleteIconVisibility(isDeleteIconVisibility)
            .build()

        if (stories.isNotEmpty()) {
            reelsViewBuilder.show()
        }
    }

    fun goToProfileFragment(userId: Int) {
        val intent = Intent(requireContext(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_PROFILE_FRAGMENT.type)
        intent.putExtra("userId", userId)
        requireActivity().startActivity(intent)
    }

    fun goToChatListFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_LIST_CHAT_FRAGMENT.type)
        startActivity(intent)
    }

    fun goToSearchFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_SEARCH_FRAGMENT.type)
        startActivity(intent)
    }

    fun goToLoginFragment() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }

    fun goToShareFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_SHARE_FRAGMENT.type)
        startActivity(intent)
    }

    fun goToNotificationFragment() {
        val intent = Intent(requireActivity(), OthersActivity::class.java)
        intent.putExtra("viewType", Constants.ViewType.VIEW_NOTIFICATION_FRAGMENT.type)
        startActivity(intent)
    }

    open fun goToRecyclerViewTop() {}

    fun showAlertDialog(message: String, listener: ConfirmDialogListener) {
        ConfirmDialog(requireActivity(), message, listener).show()
    }
}