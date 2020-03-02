package com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.colan.kindercare.R
import com.colan.kindercare.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_report_salary_filter_bottom_sheet.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ReportSalaryFilterBottomSheet(private val onSalaryFilterApplyListner: OnSalaryFilterApplyListner) :
    BottomSheetDialogFragment() {

    var selectedUserType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.colan.kindercare.R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            com.colan.kindercare.R.layout.fragment_report_salary_filter_bottom_sheet,
            container,
            false
        )
        val cancel = v.findViewById<AppCompatImageView>(com.colan.kindercare.R.id.close_label)

        isCancelable = false

        cancel.setOnClickListener {
            dismiss()
        }


        return v
    }

    private fun setupFilterRadioButtons() {
        user_rg.setOnCheckedChangeListener { p0, checkedId ->
            val rb = p0?.findViewById(checkedId) as RadioButton
            selectedUserType = getUserTypeByName(rb.text.toString())
        }
    }


    private fun getUserTypeByName(type: String): String {
        when (type) {
            resources.getString(R.string.teacher) -> {
                return Constants.TEACHER_TYPE
            }
            resources.getString(R.string.all) -> {
                return Constants.STUDENT_TYPE
            }
            resources.getString(R.string.other) -> {
                return Constants.ADMIN_TYPE
            }
        }
        return ""
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etFromDate.setOnClickListener {
            datepicker()
        }
        etToDate.setOnClickListener {
            datepicker2()
        }
        apply_btn.setOnClickListener {
            onSalaryFilterApplyListner.getSalaryFilterAppliedDate(
                etFromDate.text.toString(),
                etToDate.text.toString(),
                selectedUserType
            )
            dismiss()
        }
        setupFilterRadioButtons()

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

    interface OnSalaryFilterApplyListner {
        fun getSalaryFilterAppliedDate(fromDate: String, toDate: String, userType: String?)
    }

}

