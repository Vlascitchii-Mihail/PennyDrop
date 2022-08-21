package com.bignerdranch.android.criminalintent

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment(){

    //using callback to transfer data to CrimeFragment
//    interface Callbacks {
//        fun onDateSelected(date: Date)
//    }

    //returns Dialog with the current data
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Слушатель, используемый для указания того, что пользователь закончил выбор даты.
        /**
         * @param _: DatePicker - unused parameter
         */
        val dateListener = DatePickerDialog.OnDateSetListener {
            _: DatePicker, year: Int, month: Int, day: Int ->
            val resultDate: Date = GregorianCalendar(year, month, day).time

            //Устанавливает данный результат для requestKey. Этот результат будет доставлен в
            // FragmentResultListener, который вызывается для setFragmentResultListener с тем же ключом requestKey.
            //bundleOf() - Возвращает новый Bundle с заданными парами ключ/значение в качестве элементов.
            setFragmentResult(REQUEST_KEY, bundleOf("returnData" to resultDate))

        }

        //getting Date from fragment arguments
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()

        //setting date of crime
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        //DatePickerDialog constructor
        /**
         * @param requireContext() - returns context object
         * @param dateListener - Date listener
         */
        return DatePickerDialog(requireContext(), dateListener, initialYear, initialMonth, initialDay)
    }

    companion object {

        //transfer data from CrimeFragment to DatePickerFragment using the fragment's arguments
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply {

                //setting the data to DatePickerFragment arguments
                arguments = args
            }
        }
    }
}