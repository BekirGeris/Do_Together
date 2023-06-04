package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentReportBinding
import com.example.dotogether.model.request.ReportRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.viewmodel.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ReportViewModel by viewModels()
    private lateinit var binding: FragmentReportBinding

    private var targetId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReportBinding.inflate(layoutInflater)

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

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)
        binding.sendReport.setOnClickListener(this)

        targetId = arguments?.getInt(Constants.TARGET_ID)
    }

    private fun initObserve() {
        viewModel.sendReport.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(it.message)
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        if (navController != null) {
            when (v) {
                binding.backBtn -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                binding.sendReport -> {
                    sendReport()
                }
            }
        }
    }

    private fun sendReport() {
        val reportRequest = ReportRequest(getSelectedRadioButtonValue().toString(), binding.commentTxt.text.toString())
        targetId?.let { viewModel.sendReport(it, reportRequest) }
    }

    private fun getSelectedRadioButtonValue(): Int {
        val checkedRadioButtonId: Int = binding.radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = binding.radioGroup.findViewById(checkedRadioButtonId)
        return binding.radioGroup.indexOfChild(radioButton)
    }
}