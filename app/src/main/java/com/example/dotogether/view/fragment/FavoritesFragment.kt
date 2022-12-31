package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.FragmentFavoritesBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FavoritesFragment : BaseFragment() {

    private val viewModel: LibraryViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var targetAdapter: TargetAdapter
    private val targets = ArrayList<Target>()

    private var justOneWork = true

    private var nextPage = "2"
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoritesBinding.inflate(layoutInflater)

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
        if (justOneWork) {
            initObserve()
            justOneWork = false
        }
    }

    private fun initViews() {
        targetAdapter = TargetAdapter(targets)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = targetAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.likeTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { response ->
                        response.data?.let { list ->
                            if (list.isEmpty()) {
                                binding.activityErrorView.visibility = View.VISIBLE
                            }
                            targets.clear()
                            targets.addAll(list)
                            targetAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    binding.activityErrorView.visibility = View.VISIBLE
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.getMyLikeTargets()
        viewModel.nextLikeTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { response ->
                        response.data?.let { list ->
                            targets.addAll(list)
                            targetAdapter.notifyDataSetChanged()
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
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1)) {
                    viewModel.getNextMyLikeTargets(nextPage)
                    binding.targetRv.removeOnScrollListener(scrollListener)
                }
            }
        }
        binding.targetRv.addOnScrollListener(scrollListener)
    }
}