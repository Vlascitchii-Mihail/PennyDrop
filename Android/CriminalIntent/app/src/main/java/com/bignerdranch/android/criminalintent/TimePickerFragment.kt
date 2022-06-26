package com.bignerdranch.android.criminalintent

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.widget.TimePicker
import androidx.core.os.bundleOf
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import androidx.fragment.app.setFragmentResult

private const val ARG_TIME = "time"

class TimePickerFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()
        val initialHours = calendar.get(Calendar.HOUR)
        val initialMinutes = calendar.get(Calendar.MINUTE)
//        val seconds = calendar.get(Calendar.SECOND)
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker,  hours: Int, minutes: Int ->
            val resultTime : Date = GregorianCalendar(initialYear, initialMonth, initialDay, hours, minutes).time
            setFragmentResult(REQUEST_KEY + 1, bundleOf("returnTime" to resultTime))
        }

        return TimePickerDialog(requireContext(), timeListener, initialHours, initialMinutes, true)
    }

    companion object {
        fun newInstanceTime(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }
            return TimePickerFragment().apply { arguments = args }
        }
    }
}