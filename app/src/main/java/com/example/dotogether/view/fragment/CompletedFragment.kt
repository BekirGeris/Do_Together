package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.FragmentCompletedBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class CompletedFragment : BaseFragment(), HolderListener.TargetHolderListener {

    private val viewModel: LibraryViewModel by viewModels()
    private lateinit var binding: FragmentCompletedBinding

    private lateinit var targetAdapter: TargetAdapter
    private val targets = ArrayList<Target>()

    private var justOneWork = true

    private var nextPage = "2"
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCompletedBinding.inflate(layoutInflater)

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
        targetAdapter = TargetAdapter(targets, this)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = targetAdapter

        binding.swipeLyt.setOnRefreshListener {
            viewModel.getMyDoneTargets()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.likeJoinLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { updateTarget ->
                        val newTargets = ArrayList<Target>()
                        targets.mapTo(newTargets) {t -> if (updateTarget.id == t.id) updateTarget else t}
                        targets.clear()
                        targets.addAll(newTargets)
                        targetAdapter.notifyDataSetChanged()
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        viewModel.doneTargets.observe(viewLifecycleOwner) {
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
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }

        viewModel.getMyDoneTargets()
    }

    private fun setRecyclerViewScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1)) {
                    viewModel.getNextMyDoneTargets(nextPage)
                    binding.targetRv.removeOnScrollListener(scrollListener)
                }
            }
        }
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