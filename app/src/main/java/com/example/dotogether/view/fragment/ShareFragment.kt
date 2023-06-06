package com.example.dotogether.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
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
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.example.dotogether.model.Basket
import com.example.dotogether.model.Tag
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.collections.ArrayList
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.UpdateTargetRequest

@AndroidEntryPoint
class ShareFragment : BaseFragment(), View.OnClickListener, DateCallback {

    private val viewModel: ShareViewModel by viewModels()
    private lateinit var binding: FragmentShareBinding
    private lateinit var periodDialogBinding: BottomSheetPeriodBinding
    private lateinit var customPeriodBinding: BottomSheetCustomPeriodBinding

    private lateinit var periodDialog: BottomSheetDialog
    private lateinit var customPeriodDialog: BottomSheetDialog

    private val datePickerFragment = DatePickerFragment(this)
    private var imageBase64: String? = null

    private var isSearching = false
    private var isEdit = false
    private var targetId: Int? = null

    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions  ->
        var isGrantedGalleryAndCamera = true

        permissions.forEach { actionMap ->
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) && actionMap.key == Manifest.permission.READ_MEDIA_IMAGES ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) && actionMap.key == Manifest.permission.READ_EXTERNAL_STORAGE ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
                actionMap.key == Manifest.permission.CAMERA ->
                    if (!actionMap.value) {
                        isGrantedGalleryAndCamera = false
                    }
            }
        }

        if (isGrantedGalleryAndCamera) {
            startImageMaker()
        } else {
            showToast(getString(R.string.permissions_must_be_granted))
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
                showToast(getString(R.string.task_canceled))
            }
        }
    }

    private fun changeImage(fileUri: Uri) {
        val filePath = fileUri.toString().replace(Constants.FILE_PATH_FIRST, "")

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
        isEdit = arguments?.getBoolean(Constants.IS_EDIT) ?: false
        targetId = arguments?.getInt(Constants.TARGET_ID)

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
        binding.addTagBtn.setOnClickListener(this)

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
        binding.tagEditTxt.addTextChangedListener{ editTextChange(binding.tagEditTxt) }

        setSwitchVisibilityDesc(false)
        binding.switchVisibility.setOnCheckedChangeListener { _, isChecked ->
            setSwitchVisibilityDesc(isChecked)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.period.observe(viewLifecycleOwner) {
            binding.periodDecs.text = it
        }
        viewModel.createTarget.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(it.message)
                    val basket = viewModel.getCurrentBasketSync() ?: Basket()
                    basket.refreshType = Constants.CREATE_TARGET
                    viewModel.updateBasket(basket)
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
            }
        }
        viewModel.tags.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                    it.data?.let { list ->
                        initChipGroup(list, binding.scrollGroup)
                    }
                }
                is Resource.Error -> {
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                }
                is Resource.Loading -> {

                }
            }
        }
        viewModel.target.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    dialog.hide()
                    it.data?.let { target -> updateViewWithTargetForEdit(target) }
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
            }
        }
        viewModel.updateTarget.observe(viewLifecycleOwner) {
            when (it) {
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
            }
        }
        if (isEdit) {
            targetId?.let { viewModel.getTarget(it) }
        }
    }

    private fun updateViewWithTargetForEdit(target: Target) {
        target.img?.let {
            RuntimeHelper.glideForImage(requireContext()).load(it).into(binding.selectImage)
            binding.selectImage.setPadding(20, 5, 20, 5)
        }
        target.tags?.let { tags ->
            val tagList = tags.split(",").map { Tag(it) }.toCollection(ArrayList()).filter { it.name.isNotEmpty() }
            initChipGroup(ArrayList(tagList), binding.reflowGroup)
        }
        binding.targetEditTxt.setText(target.target)
        binding.descriptionEditTxt.setText(target.description)
        binding.periodDecs.text = target.period
        binding.startDateTxt.text = target.start_date
        binding.finishDateTxt.text = target.end_date

        target.period?.let {
            when (it) {
                Constants.DAILY -> setPeriodCheckView(periodDialogBinding.dailyCheck)
                Constants.WEEKLY -> setPeriodCheckView(periodDialogBinding.weeklyCheck)
                Constants.MONTHLY -> setPeriodCheckView(periodDialogBinding.monthlyCheck)
                Constants.MONDAY_TO_FRIDAY -> setPeriodCheckView(periodDialogBinding.mondayToFridayCheck)
                else -> {
                    setPeriodCheckView(periodDialogBinding.customCheck)
                    setCustomPeriodCheckView(it)
                }
            }
        }

        binding.switchVisibility.isChecked = target.private == true
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.backBtn -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            binding.selectImage -> {
                requestPermissionsForImagePicker()
            }
            binding.uploadBtn -> {
                var tags = ""
                var tagCount = 0
                binding.reflowGroup.children.toList().forEach {
                    tags += "${(it as Chip).text},"
                    tagCount++
                }
                validTarget()
                validDescription()
                if (tagCount !in 1..4) {
                    showToast(getString(R.string.error_tag_message))
                    return
                }
                if (validTarget() && validDescription()) {
                    var endDate = binding.finishDateTxt.text.toString()
                    if (endDate == getString(R.string.forever)) {
                        endDate = getString(R.string.year_2050)
                    }
                    if (isEdit) {
                        uploadTarget(
                            binding.targetEditTxt.text.toString(),
                            binding.descriptionEditTxt.text.toString(),
                            binding.periodDecs.text.toString(),
                            binding.startDateTxt.text.toString(),
                            endDate,
                            imageBase64,
                            tags)
                    } else {
                        shareTarget(
                            binding.targetEditTxt.text.toString(),
                            binding.descriptionEditTxt.text.toString(),
                            binding.periodDecs.text.toString(),
                            binding.startDateTxt.text.toString(),
                            endDate,
                            imageBase64,
                            tags
                        )
                    }
                }
            }
            binding.periodLyt -> {
                periodDialog.tryShow()
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
            binding.addTagBtn -> {
                val tag = binding.tagEditTxt.text.toString().trim()
                if (tag.isNotEmpty() && tag.replace(" ", "").isNotEmpty()) {
                    addChipToChipGroup(tag, binding.reflowGroup)
                    binding.tagEditTxt.setText("")
                }
            }
            periodDialogBinding.daily -> {
                 viewModel.period.value = Constants.DAILY
                setPeriodCheckView(periodDialogBinding.dailyCheck)
            }
            periodDialogBinding.weekly -> {
                 viewModel.period.value = Constants.WEEKLY
                setPeriodCheckView(periodDialogBinding.weeklyCheck)
            }
            periodDialogBinding.monthly -> {
                 viewModel.period.value = Constants.MONTHLY
                setPeriodCheckView(periodDialogBinding.monthlyCheck)
            }
            periodDialogBinding.mondayToFriday -> {
                 viewModel.period.value = Constants.MONDAY_TO_FRIDAY
                setPeriodCheckView(periodDialogBinding.mondayToFridayCheck)
            }
            periodDialogBinding.custom -> {
                customPeriodDialog.tryShow()
                periodDialog.dismiss()
            }
            customPeriodBinding.cancel -> {
                customPeriodDialog.dismiss()
            }
            customPeriodBinding.confirm -> {
                fillPeriodDescWithCheckBox()
                setPeriodCheckView(periodDialogBinding.customCheck)
                customPeriodDialog.dismiss()
            }
        }
    }

    private fun fillPeriodDescWithCheckBox() {
        var s = ""
        val customPeriodMap = mapOf(
            Pair(customPeriodBinding.mondayRadio, Constants.MON),
            Pair(customPeriodBinding.tuesdayRadio, Constants.TUE),
            Pair(customPeriodBinding.wednesdayRadio, Constants.WED),
            Pair(customPeriodBinding.thursdayRadio, Constants.THU),
            Pair(customPeriodBinding.fridayRadio, Constants.FRI),
            Pair(customPeriodBinding.saturdayRadio, Constants.SAT),
            Pair(customPeriodBinding.sundayRadio, Constants.SUN)
        )

        customPeriodMap.forEach {
            if (it.key.isChecked) {
                s += it.value + " "
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
        periodDialog.dismiss()
    }

    private fun setCustomPeriodCheckView(period: String) {
        val days = listOf(
            Constants.MON to customPeriodBinding.mondayRadio,
            Constants.TUE to customPeriodBinding.tuesdayRadio,
            Constants.WED to customPeriodBinding.wednesdayRadio,
            Constants.THU to customPeriodBinding.thursdayRadio,
            Constants.FRI to customPeriodBinding.fridayRadio,
            Constants.SAT to customPeriodBinding.saturdayRadio,
            Constants.SUN to customPeriodBinding.sundayRadio
        )

        days.forEach { (day, radioButton) ->
            if (period.contains(day)) radioButton.isChecked = true
        }
    }

    private fun requestPermissionsForImagePicker() {
        PermissionUtil.requestPermissions(
            requireActivity(),
            binding.root,
            requestMultiplePermissions,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE,
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

    private fun shareTarget(target: String, description: String, period: String, start_date: String, end_date: String, img: String?, tags: String) {
        val createTargetRequest = CreateTargetRequest(target, description, period, start_date, end_date, img, tags, binding.switchVisibility.isChecked)
        viewModel.createTarget(createTargetRequest)
    }

    private fun uploadTarget(target: String, description: String, period: String, start_date: String, end_date: String, img: String?, tags: String) {
        val updateTargetRequest = UpdateTargetRequest(target, description, period, start_date, end_date, img, tags, binding.switchVisibility.isChecked)
        targetId?.let { viewModel.updateTarget(it, updateTargetRequest) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun editTextChange(v: EditText) {
        when(v) {
            binding.targetEditTxt -> {
                validTarget()
            }
            binding.descriptionEditTxt -> {
                validDescription()
            }
            binding.tagEditTxt -> {
                val tag = binding.tagEditTxt.text.toString().trim()
                if (tag.isNotEmpty() && tag.replace(" ", "").isNotEmpty()) {
                    if (!isSearching) {
                        binding.linearIndicator.visibility = View.VISIBLE
                        isSearching = true
                        viewModel.searchTag(SearchRequest(tag))
                    }
                } else {
                    binding.scrollGroup.removeAllViews()
                }
            }
        }
    }

    private fun validTarget() : Boolean {
        return if (binding.targetEditTxt.text.toString().isEmpty()) {
            binding.targetEditLyt.error = getString(R.string.required)
            false
        } else {
            binding.targetEditLyt.error = null
            true
        }
    }

    private fun validDescription() : Boolean {
        return if (binding.descriptionEditTxt.text.toString().isEmpty()) {
            binding.descriptionEditLyt.error = getString(R.string.required)
            false
        } else {
            binding.descriptionEditLyt.error = null
            true
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
                binding.tagEditTxt.setText("")
                chipGroup.removeView(chip)
                addChipToChipGroup(text, binding.reflowGroup)
            }
        } else {
            chip.isCloseIconVisible = true
            chip.setOnCloseIconClickListener { chipGroup.removeView(chip) }
        }
        chipGroup.addView(chip)
    }

    private fun setSwitchVisibilityDesc(isChecked: Boolean) {
        val visibilityDesc = if (isChecked) {
            getString(R.string.switch_visibility_private_desc)
        } else {
            getString(R.string.switch_visibility_public_desc)
        }
        binding.switchVisibility.text = createSpannableString(visibilityDesc)
    }

    private fun createSpannableString(visibilityDesc: String): SpannableString {
        val title = visibilityDesc.substringBefore(" ")
        val spannableString = SpannableString(visibilityDesc)
        val startIndex = visibilityDesc.indexOf(title)
        spannableString.setSpan(RelativeSizeSpan(1.2f), startIndex, startIndex + title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}