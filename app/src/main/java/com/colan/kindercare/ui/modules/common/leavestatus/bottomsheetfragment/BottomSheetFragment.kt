package com.colan.kindercare.ui.modules.common.leavestatus.bottomsheetfragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatImageView
import com.colan.kindercare.R
import com.colan.kindercare.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_leave_request.*
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetFragment(private val onLeaveFilter : OnLeaveFilter) :
    BottomSheetDialogFragment() {

    var selectedLeaveType: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.bottom_sheet_leave_request, container, false)
        isCancelable = false
        val cancel = v.findViewById<AppCompatImageView>(R.id.close_label)

        isCancelable = false

        cancel.setOnClickListener {
            dismiss()
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etFromDate.setOnClickListener {
            datepicker()
        }
        etToDate.setOnClickListener {
            datepicker2()
        }
        btnSubmit.setOnClickListener {
            onLeaveFilter.getLeaveFilterAppliedData(
                etFromDate.text.toString(),
                etToDate.text.toString(),
                selectedLeaveType
            )
            dismiss()
        }
        setupFilterRadioButtons()

    }


    private fun setupFilterRadioButtons() {
        leave_rg.setOnCheckedChangeListener { p0, checkedId ->
            val rb = p0?.findViewById(checkedId) as RadioButton
            selectedLeaveType = getUserTypeByName(rb.text.toString())
        }
    }


    private fun getUserTypeByName(type: String): String {
        when (type) {
            resources.getString(R.string.all) -> {
                return Constants.ALLL
            }
            resources.getString(R.string.pending) -> {
                return Constants.PENDING
            }
            resources.getString(R.string.approved) -> {
                return Constants.APPROVED
            }
            resources.getString(R.string.rejected) -> {
                return Constants.REJECTED
            }
            resources.getString(R.string.cancelled) -> {
                return Constants.CANCEL
            }
        }
        return ""
    }


    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0

    fun datepicker() {

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(year, monthOfYear, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd");
                val strDate = format.format(c.getTime())

                etFromDate.setText(
                    strDate
                )

            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

    fun datepicker2() {

        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                c.set(year, monthOfYear, dayOfMonth)
                val format = SimpleDateFormat("yyyy-MM-dd");
                val strDate = format.format(c.getTime())

                etToDate.setText(
                    strDate
                )

            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    interface OnLeaveFilter {
        fun getLeaveFilterAppliedData(fromDate: String, toDate: String, userType: String?)
    }


}

