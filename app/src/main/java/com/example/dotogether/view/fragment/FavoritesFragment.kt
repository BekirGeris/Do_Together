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
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FavoritesFragment : BaseFragment(), HolderListener.TargetHolderListener {

    private val viewModel: LibraryViewModel by viewModels()
    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var targetAdapter: TargetAdapter
    private val targets = ArrayList<Target>()

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1)) {
                viewModel.getNextMyLikeTargets(nextPage)
                binding.targetRv.removeOnScrollListener(this)
            }
        }
    }

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
        initObserve()
    }

    private fun initViews() {
        targetAdapter = TargetAdapter(targets, this)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = targetAdapter

        binding.swipeLyt.setOnRefreshListener {
            binding.targetRv.removeOnScrollListener(scrollListener)
            viewModel.getMyLikeTargets()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.updateTarget.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { updateTarget ->
                        val newTargets = ArrayList<Target>()
                        targets.mapTo(newTargets) {t -> if (updateTarget.id == t.id) updateTarget else t}
                        targets.clear()
                        targets.addAll(newTargets.filter {it.is_liked == true})
                        targetAdapter.notifyDataSetChanged()
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
        viewModel.likeTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
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
                    binding.swipeLyt.isRefreshing = false
                    binding.activityErrorView.visibility = View.VISIBLE
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing) {
                        dialog.show()
                    }
                }
                else -> {}
            }
        }
        viewModel.getMyLikeTargets()
        viewModel.nextLikeTargets.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.nextProgressBar.visibility = View.GONE
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
                    binding.nextProgressBar.visibility = View.GONE
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    binding.nextProgressBar.visibility = View.VISIBLE
                    binding.targetRv.scrollToPosition(targets.size - 1)
                }
                else -> {}
            }
        }
    }

    private fun setRecyclerViewScrollListener() {
        binding.targetRv.addOnScrollListener(scrollListener)
    }

    override fun like(binding: ItemTargetBinding, target: Target) {
        viewModel.likeTarget(target.id!!)
    }

    override fun join(binding: ItemTargetBinding, target: Target) {
        viewModel.joinTarget(target.id!!)
    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {
        viewModel.unLikeTarget(target.id!!)
    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {
        viewModel.unJoinTarget(target.id!!)
    }
}