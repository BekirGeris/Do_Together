package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentTargetBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.viewmodel.TargetViewModel
import java.util.ArrayList

class TargetFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetBinding

    private lateinit var memberAdapter: MemberAdapter
    private val members = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTargetBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)

        for (i in 1..100) {
            members.add(User())
        }

        memberAdapter = MemberAdapter(members, true)

        binding.memberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.memberRv.adapter = memberAdapter

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        val navController = v?.findNavController()
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    navController.popBackStack().let {
                        if (!it) {
                            activity?.finish()
                        }
                    }
                }
                binding.moreSettingBtn -> {

                }
            }
        }
    }
}