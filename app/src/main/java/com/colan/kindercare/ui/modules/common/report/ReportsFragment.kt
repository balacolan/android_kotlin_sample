package com.colan.kindercare.ui.modules.common.report


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.databinding.FragmentReportsBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.report.attendanceReport.AttendanceReportFragment
import com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet.ReportAttendanceFilterBottomSheet
import com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet.ReportSalaryFilterBottomSheet
import com.colan.kindercare.ui.modules.common.report.salaryReport.SalaryReportFragment
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Singleton


/**
 * A simple [Fragment] subclass.
 */
class ReportsFragment : BaseFragment<FragmentReportsBinding, ReportVM>(), IRadioListener,
    ReportAttendanceFilterBottomSheet.OnFilterApplyListner,
    ReportSalaryFilterBottomSheet.OnSalaryFilterApplyListner {


    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.reportVM
    override val layoutId: Int
        get() = com.colan.kindercare.R.layout.fragment_reports
    override val viewModel: ReportVM
        get() = ViewModelProviders.of(this).get(ReportVM::class.java)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(com.colan.kindercare.R.string.reports))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(
            com.colan.kindercare.R.id.report_attendance_container,
            AttendanceReportFragment(),
            "Attendance",
            "Attendance"
        )
        viewDataBinding?.filterIv?.setOnClickListener {
            if (viewModel.attendendaceSelected.get()!!) {
                val bottomSheetFragment = ReportAttendanceFilterBottomSheet(this)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.getTag())
            } else {
                val bottomSheetFragment = ReportSalaryFilterBottomSheet(this)
                bottomSheetFragment.show(fragmentManager!!, bottomSheetFragment.getTag())
            }

        }
    }



    override fun onRadioButtonClick(id: Int) {
        when (id) {
            com.colan.kindercare.R.id.attendace_rb -> {
                viewModel.attendendaceSelected.set(true)
                viewModel.salarySelected.set(false)
                showFragment(AttendanceReportFragment())
            }
            com.colan.kindercare.R.id.salary_rb -> {
                viewModel.attendendaceSelected.set(false)
                viewModel.salarySelected.set(true)
                showFragment(SalaryReportFragment())
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(com.colan.kindercare.R.id.report_attendance_container, fragment).commit()
    }

    override fun getFilterAppliedDate(
        fromDate: String,
        toDate: String,
        userType: String?,
        classType: String?,
        sectionType: String?
    ) {
        if (viewModel.attendendaceSelected.get()!!) {
            val fragment = AttendanceReportFragment()
            val bundle = Bundle()
            bundle.apply {
                putString("fromDate", fromDate)
                putString("toDate", toDate)
                putString("userType", userType)
                putString("classType", classType)
                putString("sectionType", sectionType)
            }
            fragment.setArguments(bundle)
            showFragment(fragment)
        }
    }

    override fun getSalaryFilterAppliedDate(fromDate: String, toDate: String, userType: String?) {
        if (viewModel.salarySelected.get()!!) {
            val fragment = SalaryReportFragment()
            val bundle = Bundle()
            bundle.putString("fromDate", fromDate)
            bundle.putString("toDate", toDate)
            bundle.putString("userType", userType)
           /* bundle.putString("classType", classType)
            bundle.putString("sectionType", sectionType)*/
            fragment.setArguments(bundle)
            showFragment(fragment)
        }
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

}
