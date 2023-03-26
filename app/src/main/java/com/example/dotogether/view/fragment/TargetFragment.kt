package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentTargetBinding
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.Tag
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.displayText
import com.example.dotogether.util.helper.RuntimeHelper.setViewProperties
import com.example.dotogether.util.helper.RuntimeHelper.tryParse
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.view.container.DayViewContainer
import com.example.dotogether.viewmodel.TargetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TargetFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetBinding

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var memberAdapter: MemberAdapter

    private var targetId: Int? = null
    private lateinit var target: Target
    private var myUserId: Int? = null

    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val currentMonth = YearMonth.now()
    private val startMonth = currentMonth.minusMonths(100)
    private val endMonth = currentMonth.plusMonths(100)
    private val daysOfWeek = daysOfWeek()

    var lastDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTargetBinding.inflate(layoutInflater)
        dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(dialogBinding.root.context, R.style.BottomSheetDialogTheme)

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
        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.backBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.joinBtn.setOnClickListener(this)
        binding.goToChatBtn.setOnClickListener(this)
        binding.doItBtn.setOnClickListener(this)
        binding.allMemberTxt.setOnClickListener(this)

        dialogBinding.share.visibility = View.VISIBLE
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.edit.setOnClickListener(this)
        binding.calendar.visibility = View.GONE

        binding.memberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        targetId = arguments?.getInt("targetId")
    }

    private fun initObserve() {
        viewModel.getMyUserFromLocale().observe(viewLifecycleOwner) {
            myUserId = it.id
        }
        viewModel.target.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { target ->
                        this.target = target
                        setViewWithTarget(target)
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(it.message)
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
        viewModel.updateTarget.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { target ->
                        this.target = target
                        setViewWithTarget(target)
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
        viewModel.doneTarget.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    dialog.hide()
                }
                is Resource.Error -> {
                    showToast(it.message)
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
        targetId?.let {
            viewModel.getTarget(it)
        }
    }

    @SuppressLint("ResourceType")
    private fun setViewWithTarget(target: Target) {
        target.users?.let { members ->
            memberAdapter = MemberAdapter(members, true)
            binding.memberRv.adapter = memberAdapter
        }

        binding.joinBtn.visibility = if (target.is_joined == false) View.VISIBLE else View.GONE
        binding.doItBtn.visibility = if (target.is_joined == true) View.VISIBLE else View.GONE
        binding.calendar.visibility = if (target.is_joined == true) View.VISIBLE else View.GONE
        binding.goToChatBtn.visibility = if (target.is_joined == true) View.VISIBLE else View.GONE

        binding.target.text = target.target
        binding.description.text = target.description
        binding.period.text = target.period
        binding.startDate.text = target.start_date
        binding.endDate.text = if (target.end_date == getString(R.string.forever_date)) getString(R.string.forever) else target.end_date

        if (target.img != null) {
            RuntimeHelper.glideForImage(requireContext()).load(target.img).into(binding.targetImage)
        } else {
            binding.targetImage.background = ContextCompat.getDrawable(requireContext(), R.drawable.pilgrim)
        }

        setupMonthCalendar()

        if (myUserId == target.admin?.id) {
            dialogBinding.edit.visibility = View.VISIBLE
        } else {
            dialogBinding.edit.visibility = View.GONE
        }

        target.tags?.let { tags ->
            binding.tagsTxt.visibility = View.VISIBLE
            val tagList = tags.split(",").map { Tag(it) }.toCollection(ArrayList()).filter { it.name.isNotEmpty() }
            initChipGroup(ArrayList(tagList), binding.reflowGroup)
        }

        targetId?.let {
            viewModel.getActions(it).observe(viewLifecycleOwner) { resource ->
                when(resource) {
                    is Resource.Success -> {
                        resource.data?.forEach { action ->
                            action.created_at?.let { time ->
                                lastDate = Constants.DATE_FORMAT_3.tryParse(time)
                                lastDate?.let { d ->
                                    selectedDates.add(RuntimeHelper.convertDateToLocalDate(d))
                                }
                            }
                        }
                        setupMonthCalendar()

                        binding.doItBtn.setViewProperties(isDoItBTNOpen())
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                binding.moreSettingBtn -> {
                    bottomSheetDialog.tryShow()
                }
                binding.goToChatBtn -> {
                    target.chat_id?.let { navController.navigate(TargetFragmentDirections.actionChatFragment(isGroup =  true, chatId = it, chatUser = OtherUser(target))) }
                }
                binding.joinBtn -> {
                    viewModel.joinTarget(targetId!!)
                }
                binding.doItBtn -> {
                    selectedDates.add(LocalDate.now())
                    setupMonthCalendar()
                    binding.doItBtn.setViewProperties(false)
                    targetId?.let { targetId -> viewModel.doneTarget(targetId) }
                }
                binding.allMemberTxt -> {
                    targetId?.let { navController.navigate(TargetFragmentDirections.actionTargetFragmentToTargetMembersFragment(targetId = it, isAdmin = target.admin?.id == myUserId)) }
                }
                dialogBinding.share -> {
                    bottomSheetDialog.dismiss()
                    targetId?.let { it1 -> RuntimeHelper.shareTargetLink(requireContext(), it1) }
                }
                dialogBinding.delete -> {
                    bottomSheetDialog.dismiss()
                }
                dialogBinding.edit -> {
                    bottomSheetDialog.dismiss()
                    targetId?.let { navController.navigate(TargetFragmentDirections.actionShareFragment(isEdit = true, targetId = it)) }
                }
                else -> {}
            }
        }
    }

    private fun setupMonthCalendar() {
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                container.textView.alpha = if (data.position == DayPosition.MonthDate) 1f else 0.5f
                container.day = data
                bindDate(data.date, container.textView)
            }
        }
        binding.calendarView.monthScrollListener = { updateTitle() }
        binding.calendarView.setup(startMonth, endMonth, daysOfWeek.first())
        binding.calendarView.scrollToMonth(currentMonth)
        updateTitle()
    }

    private fun updateTitle() {
        val month = binding.calendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.yearText.text = month.year.toString()
        binding.monthText.text = month.month.displayText(short = false)
    }

    private fun bindDate(date: LocalDate, textView: TextView) {
        textView.text = date.dayOfMonth.toString()
        when {
            today == date && selectedDates.contains(date) -> {
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.green_kare_blue_cycle)
            }
            selectedDates.contains(date) -> {
                textView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_green))
            }
            today == date -> {
                textView.background = ContextCompat.getDrawable(requireContext(), R.drawable.border_circle)
            }
            else -> {
                textView.background = null
            }
        }
    }

    private fun initChipGroup(tags: ArrayList<Tag>, chipGroup: ChipGroup) {
        chipGroup.removeAllViews()
        for (tag in tags) {
            val chip = Chip(context)
            chip.text = tag.name
            chipGroup.addView(chip)
        }
    }

    private fun isDoItBTNOpen(): Boolean {
        lastDate?.let {
            when (target.period) {
                Constants.DAILY -> {
                    return !isDateInCurrentDay(it)
                }
                Constants.WEEKLY -> {
                    return !isDateInCurrentWeek(it)
                }
                Constants.MONTHLY -> {
                    return !isDateInCurrentMonth(it)
                }
                else -> {
                    return !isDateInRangeForWeek(it, getRangeForPeriod(target.period))
                }
            }
        }
        return true
    }

    private fun getRangeForPeriod(period: String?): List<Int> {
        val list = ArrayList<Int>()
        period?.let {
            if (it == Constants.MONDAY_TO_FRIDAY) { return  listOf(2, 3, 4, 5, 6) }
            if (it.contains(Constants.MON)) { list.add(2) }
            if (it.contains(Constants.TUE)) { list.add(3) }
            if (it.contains(Constants.WED)) { list.add(4) }
            if (it.contains(Constants.THU)) { list.add(5) }
            if (it.contains(Constants.FRI)) { list.add(6) }
            if (it.contains(Constants.SAT)) { list.add(7) }
            if (it.contains(Constants.SUN)) { list.add(1) }
        }
        return list
    }

    private fun isDateInCurrentDay(date: Date): Boolean {
        val calendar = Calendar.getInstance() // şu anki takvim örneği
        val currentDate = calendar.get(Calendar.DAY_OF_YEAR) // bugünün sırası
        calendar.time = date // takvim örneğini verilen tarihle ayarla
        val dateNumber = calendar.get(Calendar.DAY_OF_YEAR) // tarihin sırası
        return currentDate == dateNumber // günler eşleşiyorsa true döndür
    }

    private fun isDateInCurrentWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance() // şu anki takvim örneği
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR) // mevcut haftanın sırası
        calendar.time = date // takvim örneğini verilen tarihle ayarla
        val dateWeek = calendar.get(Calendar.WEEK_OF_YEAR) // tarihin haftasının sırası
        return currentWeek == dateWeek // haftalar eşleşiyorsa true döndür
    }

    private fun isDateInCurrentMonth(date: Date): Boolean {
        val calendar = Calendar.getInstance() // şu anki takvim örneği
        val currentMonth = calendar.get(Calendar.MONTH) // mevcut ayın sırası
        calendar.time = date // takvim örneğini verilen tarihle ayarla
        val dateMonth = calendar.get(Calendar.MONTH) // tarihin ayının sırası
        return currentMonth == dateMonth // aylar eşleşiyorsa true döndür
    }

    private fun isDateInRangeForWeek(date: Date, weekdays: List<Int>): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.time = Date()
        val today = calendar.get(Calendar.DAY_OF_WEEK)
        return weekdays.contains(dayOfWeek) || !weekdays.contains(today)  // bulıunduğumuz gün eğer aralıkta değilde true döndürülerek butonun kapatılması sağlanır.
    }
}