package com.example.dotogether.view.container

import android.view.View
import com.example.dotogether.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View) : ViewContainer(view) {
     lateinit var day: CalendarDay
     val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}