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
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.displayText
import com.example.dotogether.util.helper.RuntimeHelper.setViewProperties
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.view.container.DayViewContainer
import com.example.dotogether.viewmodel.TargetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.MonthDayBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

@AndroidEntryPoint
class TargetFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetBinding

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var memberAdapter: MemberAdapter

    var targetId: Int? = null
    private lateinit var target: Target

    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()
    private val currentMonth = YearMonth.now()
    private val startMonth = currentMonth.minusMonths(100)
    private val endMonth = currentMonth.plusMonths(100)
    private val daysOfWeek = daysOfWeek()

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

        dialogBinding.share.visibility = View.VISIBLE
        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.edit.visibility = View.VISIBLE
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.delete.setOnClickListener(this)
        dialogBinding.edit.setOnClickListener(this)
        binding.calendar.visibility = View.GONE

        binding.memberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        targetId = arguments?.getInt("targetId")

        for (i in 1..100) {
            selectedDates.add(getDateBeforeGivenDays(getRandomNumber(300)))
        }
        setupMonthCalendar()

    }

    private fun initObserve() {
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
                }
                is Resource.Loading -> {
                    dialog.shoe()
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
                    dialog.shoe()
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
                    dialog.shoe()
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

        target.target?.let {
            binding.target.text = it
        }

        target.description?.let {
            binding.description.text = it
        }

        target.period?.let {
            binding.period.text = it
        }

        target.start_date?.let {
            binding.startDate.text = it
        }

        target.end_date?.let {
            binding.endDate.text = if (it == getString(R.string.forever_date)) getString(R.string.forever) else it
        }

        if (target.img != null) {
            RuntimeHelper.glideForImage(requireContext()).load(target.img).into(binding.targetImage)
        } else {
            binding.targetImage.background = ContextCompat.getDrawable(requireContext(), R.drawable.pilgrim)
        }

        binding.doItBtn.setViewProperties(target.action_status == "2")
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
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
                dialogBinding.share -> {
                    bottomSheetDialog.hide()
                }
                dialogBinding.delete -> {
                    bottomSheetDialog.hide()
                }
                dialogBinding.edit -> {
                    bottomSheetDialog.hide()
                }
                else -> {}
            }
        }
    }

    fun getDateBeforeGivenDays(days: Int): LocalDate {
        val now = LocalDate.now()
        return now.minusDays(days.toLong())
    }

    val randomNumbers = mutableListOf<Int>()
    fun getRandomNumber(maxDeger: Int): Int {
        if (randomNumbers.size == maxDeger) {
            println("All numbers have been generated")
            return -1
        }
        val random = (1..maxDeger).random()
        if (randomNumbers.contains(random)) {
            return getRandomNumber(maxDeger)
        }
        randomNumbers.add(random)
        return random
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
                bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
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

    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        when {
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
}