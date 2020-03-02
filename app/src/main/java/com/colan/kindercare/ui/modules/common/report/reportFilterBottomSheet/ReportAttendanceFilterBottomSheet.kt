package com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.colan.kindercare.R
import com.colan.kindercare.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_report_filter_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_report_salary_filter_bottom_sheet.apply_btn
import kotlinx.android.synthetic.main.fragment_report_salary_filter_bottom_sheet.etFromDate
import kotlinx.android.synthetic.main.fragment_report_salary_filter_bottom_sheet.etToDate
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ReportAttendanceFilterBottomSheet(private val onFilterApplyListner: OnFilterApplyListner) :
    BottomSheetDialogFragment() {

    var selectedUserType: String? = null
    var selectedClassType: String? = null
    var selectedSectionType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            com.colan.kindercare.R.layout.fragment_report_filter_bottom_sheet,
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
        class_rg.setOnCheckedChangeListener { p0, checkedId ->
            val rbClass = p0?.findViewById(checkedId) as RadioButton
            selectedClassType = getClassTypeByName(rbClass.text.toString())
            Log.d("radio", "" + rbClass.text.toString())
        }
        section_rg.setOnCheckedChangeListener { p0, checkedId ->
            val rbSection = p0?.findViewById(checkedId) as RadioButton
            selectedSectionType = getSectionTypeByName(rbSection.text.toString())
            Log.d("radio", "" + rbSection.text.toString())
        }
    }

    private fun getSectionTypeByName(sectionType: String): String? {
        when (sectionType) {
            resources.getString(R.string.a_section) -> {
                return Constants.SECTION_A
            }
            resources.getString(R.string.b_section) -> {
                return Constants.SECTION_B
            }
            resources.getString(R.string.c_section) -> {
                return Constants.SECTION_C
            }
        }
        return ""
    }

    private fun getClassTypeByName(classType: String): String? {
        when (classType) {
            resources.getString(R.string.preKg) -> {
                return Constants.PREKG
            }
            resources.getString(R.string.lkg) -> {
                return Constants.LKG
            }
            resources.getString(R.string.ukg) -> {
                return Constants.UKG
            }
        }
        return ""
    }

    private fun getUserTypeByName(type: String): String {
        when (type) {
            resources.getString(R.string.teacher) -> {
                return Constants.TEACHER_TYPE
            }
            resources.getString(R.string.student) -> {
                return Constants.STUDENT_TYPE
            }
            resources.getString(R.string.admin) -> {
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
            onFilterApplyListner.getFilterAppliedDate(
                etFromDate.text.toString(),
                etToDate.text.toString(),
                selectedUserType,
                selectedClassType,
                selectedSectionType
            )
            dismiss()
        }

        setupFilterRadioButtons()

    }

    private fun validateCredentials(): Boolean {
        when {
            etFromDate.equals(null) ->
            Toast.makeText(context, "Please select class",
                Toast.LENGTH_LONG).show()
            //etFromDate.equals(null) -> Toast(context,m"Please select class")
            //viewModel.mSectionType.get()!!.isEmpty() -> putToast("Please select section")
            else -> return true
        }
        return false
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


    interface OnFilterApplyListner {
        fun getFilterAppliedDate(
            fromDate: String,
            toDate: String,
            userType: String?,
            classType: String?,
            sectionType: String?
        )
    }


}
