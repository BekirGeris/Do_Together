package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.dotogether.databinding.FragmentAddTagBinding
import com.example.dotogether.model.Tag
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.viewmodel.ProfileViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTagFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentAddTagBinding

    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddTagBinding.inflate(layoutInflater)

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

    private fun initViews() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (!isSearching) {
                        binding.linearIndicator.visibility = View.VISIBLE
                        isSearching = true
                        viewModel.searchTag(SearchRequest(newText))
                    }
                }
                return true
            }
        })
    }

    private fun initObserve() {
        viewModel.myUserRemote.observe(viewLifecycleOwner) {resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.tags?.let {tags ->
                        val tagList = tags.split(",").map { Tag(it) }.toCollection(ArrayList())
                        initChipGroup(ArrayList(tagList), binding.reflowGroup)
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {}
                else -> {}
            }
        }
        viewModel.getMyUserFromRemote()
        viewModel.tags.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { initChipGroup(it, binding.scrollGroup) }
                    isSearching = false
                    binding.linearIndicator.visibility = View.GONE
                }
                is Resource.Error -> {
                    isSearching = false
                    binding.linearIndicator.visibility = View.GONE
                }
                is Resource.Loading -> {

                }
                else -> {}
            }
        }
    }

    private fun initChipGroup(tags: ArrayList<Tag>, chipGroup: ChipGroup) {
        chipGroup.removeAllViews()
        for (tag in tags) {
            addChipToChipGroup(tag.name, chipGroup)
        }
    }

    private fun addChipToChipGroup(text: String, chipGroup: ChipGroup) {
        val chip = Chip(context)
        chip.text = text
        if (chipGroup == binding.scrollGroup) {
            chip.setOnClickListener {
                chipGroup.removeView(chip)
                addChipToChipGroup(text, binding.reflowGroup)
            }
        } else {
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener { chipGroup.removeView(chip) }
        }
        chipGroup.addView(chip)
    }
}