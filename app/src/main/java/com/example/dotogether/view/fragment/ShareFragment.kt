package com.example.dotogether.view.fragment

import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetCustomPeriodBinding
import com.example.dotogether.databinding.BottomSheetPeriodBinding
import com.example.dotogether.databinding.FragmentShareBinding
import com.example.dotogether.model.request.CreateTargetRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.PermissionUtil
import com.example.dotogether.util.Resource
import com.example.dotogether.view.callback.DateCallback
import com.example.dotogether.viewmodel.ShareViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.example.dotogether.util.helper.RuntimeHelper

@AndroidEntryPoint
class ShareFragment : BaseFragment(), View.OnClickListener, DateCallback {

    private val viewModel: ShareViewModel by viewModels()
    private lateinit var binding: FragmentShareBinding
    private lateinit var periodDialogBinding: BottomSheetPeriodBinding
    private lateinit var customPeriodBinding: BottomSheetCustomPeriodBinding

    private lateinit var periodDialog: BottomSheetDialog
    private lateinit var customPeriodDialog: BottomSheetDialog

    val datePickerFragment = DatePickerFragment(this)
    private lateinit var imageBase64: String

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions  ->
        var isGrantedGalleryAndCamera = true

        permissions.forEach { actionMap ->
            when (actionMap.key) {
                Manifest.permission.READ_EXTERNAL_STORAGE ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
                Manifest.permission.CAMERA ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
            }
        }

        if (isGrantedGalleryAndCamera) {
            startImageMaker()
        } else {
            showToast("Gallery and camera permission must be granted!")
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val resultCode = result.resultCode
        val data = result.data

        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data!!
                changeImage(fileUri)
            }
            ImagePicker.RESULT_ERROR -> {
                showToast(ImagePicker.getError(data))
            }
            else -> {
                showToast("Task Cancelled")
            }
        }
    }

    private fun changeImage(fileUri: Uri) {
        val filePath = fileUri.toString().replace("file://", "")

        try {
            imageBase64 = RuntimeHelper.imageToBase64(filePath)

            binding.selectImage.setImageURI(fileUri)
            binding.selectImage.setPadding(20, 5, 20, 5)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun initViews() {
        binding = FragmentShareBinding.inflate(layoutInflater)
        periodDialogBinding = BottomSheetPeriodBinding.inflate(layoutInflater)
        customPeriodBinding = BottomSheetCustomPeriodBinding.inflate(layoutInflater)

        periodDialog = BottomSheetDialog(periodDialogBinding.root.context, R.style.BottomSheetDialogTheme)
        customPeriodDialog = BottomSheetDialog(customPeriodBinding.root.context, R.style.BottomSheetDialogTheme)

        periodDialog.setContentView(periodDialogBinding.root)
        customPeriodDialog.setContentView(customPeriodBinding.root)

        binding.backBtn.setOnClickListener(this)
        binding.selectImage.setOnClickListener(this)
        binding.uploadBtn.setOnClickListener(this)
        binding.periodLyt.setOnClickListener(this)
        binding.startLyt.setOnClickListener(this)
        binding.finishLyt.setOnClickListener(this)
        binding.startDateClear.setOnClickListener(this)
        binding.finishDateClear.setOnClickListener(this)
        binding.closeBtn.setOnClickListener(this)

        periodDialogBinding.daily.setOnClickListener(this)
        periodDialogBinding.weekly.setOnClickListener(this)
        periodDialogBinding.monthly.setOnClickListener(this)
        periodDialogBinding.mondayToFriday.setOnClickListener(this)
        periodDialogBinding.custom.setOnClickListener(this)

        customPeriodBinding.cancel.setOnClickListener(this)
        customPeriodBinding.confirm.setOnClickListener(this)

        binding.startDateTxt.text = Constants.DATE_FORMAT.format(System.currentTimeMillis())

        binding.targetEditTxt.addTextChangedListener{ editTextChange(binding.targetEditTxt) }
        binding.descriptionEditTxt.addTextChangedListener{ editTextChange(binding.descriptionEditTxt) }
    }

    private fun initObserve() {
        viewModel.period.observe(viewLifecycleOwner) {
            binding.periodDecs.text = it
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        when(v) {
            binding.backBtn -> {
                activity?.onBackPressed()
            }
            binding.selectImage -> {
                requestPermissionsForImagePicker()
            }
            binding.uploadBtn -> {
                validTarget()
                validDescription()
                if (validTarget() && validDescription()) {
                    var endDate = binding.finishDateTxt.text.toString()
                    if (endDate == getString(R.string.forever)) {
                        endDate = "2050/01/01"
                    }
                    shareTarget(
                        binding.targetEditTxt.text.toString(),
                        binding.descriptionEditTxt.text.toString(),
                        binding.periodDecs.text.toString(),
                        binding.startDateTxt.text.toString(),
                        endDate,
                        imageBase64
                    )
                }
            }
            binding.periodLyt -> {
                periodDialog.show()
            }
            binding.startLyt -> {
                datePickerFragment.show(parentFragmentManager, "start")
            }
            binding.finishLyt -> {
                datePickerFragment.show(parentFragmentManager, "finish")
            }
            binding.startDateClear -> {
                binding.startDateClear.visibility = View.GONE
                binding.startDateTxt.text = Constants.DATE_FORMAT.format(System.currentTimeMillis())
            }
            binding.finishDateClear -> {
                binding.finishDateClear.visibility = View.GONE
                binding.finishDateTxt.text = getString(R.string.forever)
            }
            binding.closeBtn -> {
                activity?.onBackPressed()
            }
            periodDialogBinding.daily -> {
                 viewModel.period.value = "Daily"
                setPeriodCheckView(periodDialogBinding.dailyCheck)
            }
            periodDialogBinding.weekly -> {
                 viewModel.period.value = "Weekly"
                setPeriodCheckView(periodDialogBinding.weeklyCheck)
            }
            periodDialogBinding.monthly -> {
                 viewModel.period.value = "Monthly"
                setPeriodCheckView(periodDialogBinding.monthlyCheck)
            }
            periodDialogBinding.mondayToFriday -> {
                 viewModel.period.value = "Monday to Friday"
                setPeriodCheckView(periodDialogBinding.mondayToFridayCheck)
            }
            periodDialogBinding.custom -> {
                customPeriodDialog.show()
                periodDialog.hide()
            }
            customPeriodBinding.cancel -> {
                customPeriodDialog.hide()
            }
            customPeriodBinding.confirm -> {
                fillPeriodDescWithCheckBox()
                setPeriodCheckView(periodDialogBinding.customCheck)
                customPeriodDialog.hide()
            }
        }
    }

    private fun fillPeriodDescWithCheckBox() {
        var s = ""
        val customPeriodMap = mapOf(
            Pair(customPeriodBinding.mondayRadio, customPeriodBinding.mondayTxt),
            Pair(customPeriodBinding.tuesdayRadio, customPeriodBinding.tuesdayTxt),
            Pair(customPeriodBinding.wednesdayRadio, customPeriodBinding.wednesdayTxt),
            Pair(customPeriodBinding.thursdayRadio, customPeriodBinding.thursdayTxt),
            Pair(customPeriodBinding.fridayRadio, customPeriodBinding.fridayTxt),
            Pair(customPeriodBinding.saturdayRadio, customPeriodBinding.saturdayTxt),
            Pair(customPeriodBinding.sundayRadio, customPeriodBinding.sundayTxt)
        )

        customPeriodMap.forEach {
            if (it.key.isChecked) {
                s += it.value.text.toString().substring(0, 3) + " "
            }
        }

        viewModel.period.value = s
    }

    private fun setPeriodCheckView(view: View) {
        periodDialogBinding.dailyCheck.visibility = View.GONE
        periodDialogBinding.weeklyCheck.visibility = View.GONE
        periodDialogBinding.monthlyCheck.visibility = View.GONE
        periodDialogBinding.mondayToFridayCheck.visibility = View.GONE
        periodDialogBinding.customCheck.visibility = View.GONE
        view.visibility = View.VISIBLE
        periodDialog.hide()
    }

    fun requestPermissionsForImagePicker() {
        PermissionUtil.requestPermissions(
            requireContext(),
            requireActivity(),
            binding.root,
            requestMultiplePermissions,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ) {
            startImageMaker()
        }
    }

    private fun startImageMaker() {
        ImagePicker.with(this)
            .crop(6f, 4f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .saveDir(File(context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            .createIntent {
                resultLauncher.launch(it)
            }
    }

    override fun onDate(tag: String?, year: Int, month: Int, day: Int) {
        val date = GregorianCalendar(year, month, day).time
        val strDate = Constants.DATE_FORMAT.format(date)

        when(tag) {
            "start" -> {
                binding.startDateTxt.text = strDate
                binding.startDateClear.visibility = View.VISIBLE
            }
            "finish" -> {
                binding.finishDateTxt.text = strDate
                binding.finishDateClear.visibility = View.VISIBLE
            }
        }
    }

    private fun shareTarget(target: String, description: String, period: String, start_date: String, end_date: String, img: String) {
        val createTargetRequest = CreateTargetRequest(target, description, period, start_date, end_date, img)
        viewModel.createTarget.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(it.message)
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
            }
        }
        viewModel.createTarget(createTargetRequest)
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.targetEditTxt -> {
                validTarget()
            }
            binding.descriptionEditTxt -> {
                validDescription()
            }
        }
    }

    private fun validTarget() : Boolean {
        return if (binding.targetEditTxt.text.toString().isEmpty()) {
            binding.targetEditLyt.error = "Requaried"
            false
        } else {
            binding.targetEditLyt.error = null
            true
        }
    }

    private fun validDescription() : Boolean {
        return if (binding.descriptionEditTxt.text.toString().isEmpty()) {
            binding.descriptionEditLyt.error = "Requaried"
            false
        } else {
            binding.descriptionEditLyt.error = null
            true
        }
    }
}