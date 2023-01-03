package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentSearchBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.UserAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class SearchFragment : BaseFragment(), View.OnClickListener, HolderListener.TargetHolderListener{

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding

    private lateinit var userAdapter: UserAdapter
    private lateinit var targetAdapter: TargetAdapter

    private val users = ArrayList<User>()
    private val targets = ArrayList<Target>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        initViews();
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

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews() {
        binding.searchView.onActionViewExpanded()
        binding.backBtn.setOnClickListener(this)

        for (i in 1..100) {
            users.add(User())
            targets.add(Target())
        }

        userAdapter = UserAdapter(users)
        targetAdapter = TargetAdapter(targets, this)

        binding.searchRv.layoutManager = LinearLayoutManager(context)
        binding.searchRv.adapter = userAdapter

        binding.personsTargetsRadioGrp.setOnCheckedChangeListener { radioGroup, i ->
            if (i == binding.personsBtn.id) {
                binding.searchRv.adapter = userAdapter
            } else if (i == binding.targetsBtn.id) {
                binding.searchRv.adapter = targetAdapter
            }
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when (v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                else -> {}
            }
        }
    }

    private fun initObserve() {

    }

    override fun like(binding: ItemTargetBinding, target: Target) {

    }

    override fun join(binding: ItemTargetBinding, target: Target) {

    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {

    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {

    }
}