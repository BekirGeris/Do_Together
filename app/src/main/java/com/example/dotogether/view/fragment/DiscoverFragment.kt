package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotogether.databinding.FragmentDiscoverBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.DiscoverAdapter
import com.example.dotogether.viewmodel.DiscoverViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment @Inject constructor() : BaseFragment() {

    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var binding: FragmentDiscoverBinding

    private val discovers = ArrayList<Discover>()
    private lateinit var discoverAdapter: DiscoverAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDiscoverBinding.inflate(layoutInflater)

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
        discoverAdapter = DiscoverAdapter(discovers)
        binding.discoverRv.adapter = discoverAdapter

        binding.swipeLyt.setOnRefreshListener {
            viewModel.getAllTargets()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.allTargets.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                            discovers.clear()

                            var discover = Discover()
                            discover.targets = ArrayList(list)
                            discover.title = "Hatfanın Enleri"
                            discovers.add(discover)

                            discover = Discover()
                            discover.targets = ArrayList(list)
                            discover.title = "Son Oluşturulanlar"
                            discovers.add(discover)

                            discover = Discover()
                            discover.targets = ArrayList(list)
                            discover.title = "Yeni Hedefler Keşfet"
                            discovers.add(discover)

                            discover = Discover()
                            discover.targets = ArrayList(list)
                            discover.title = "Yeni Yolculuklara Çık"
                            discovers.add(discover)

                            discoverAdapter.notifyDataSetChanged()
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
                    if (!binding.swipeLyt.isRefreshing) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.getAllTargets()
    }
}