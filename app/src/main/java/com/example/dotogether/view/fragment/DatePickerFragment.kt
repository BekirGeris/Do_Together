package com.example.dotogether.view.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.dotogether.view.callback.DateCallback
import java.util.*

class DatePickerFragment(val dateCallback: DateCallback) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dateTag: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        val dataPickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        dataPickerDialog.datePicker.minDate = System.currentTimeMillis()
        return dataPickerDialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        dateTag = tag
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        dateCallback.onDate(dateTag, year, month, day)
    }
}