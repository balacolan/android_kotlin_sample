package com.colan.kindercare.ui.modules.common.report.attendanceReport

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.adapter.BaseRecyclerViewAdapter
import com.colan.kindercare.adapter.OnDataBindCallback
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.data.remote.data.response.report.ReportAttenanceResponse
import com.colan.kindercare.databinding.FragmentAttendanceReportBinding
import com.colan.kindercare.databinding.ReportAttendanceListItemBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.modules.common.report.ReportVM
import com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet.ReportAttendanceFilterBottomSheet
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SchoolSelectionBroadCast
import com.colan.kindercare.utils.SchoolSelectionListner
import kotlinx.android.synthetic.main.filter_content_detail_layout.view.*

/**
 * A simple [Fragment] subclass.
 */
class AttendanceReportFragment : BaseFragment<FragmentAttendanceReportBinding, ReportVM>(),
    IRadioListener,
    OnDataBindCallback<ReportAttendanceListItemBinding>, SchoolSelectionListner {

    private var selectedFromDate: String? = ""
    private var selectedTodate: String? = ""
    private var selectedUserType: String? = ""
    private var selectedClass: String? = ""
    private var selectedSection: String? = ""

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<ReportAttenanceResponse, ReportAttendanceListItemBinding>? =
        null

    override val bindingVariable: Int
        get() = BR.reportVM
    override val layoutId: Int
        get() = R.layout.fragment_attendance_report
    override val viewModel: ReportVM
        get() = ViewModelProviders.of(this).get(ReportVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
        SchoolSelectionBroadCast.schoolLister =this
        val arguments = arguments
        selectedFromDate = arguments?.getString("fromDate")
        selectedTodate = arguments?.getString("toDate")
        selectedUserType = arguments?.getString("userType")
        selectedClass = arguments?.getString("classType")
        selectedSection = arguments?.getString("sectionType")

        viewModel.mFromDate.set(selectedFromDate)
        viewModel.mToDate.set(selectedTodate)
        viewModel.mUserType.set(selectedUserType)
        viewModel.mClassType.set(selectedClass)
        viewModel.mSectionType.set(selectedSection)

        viewModel.loadAttendanceReport()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedFilterText()
    }

    private fun selectedFilterText() {
        when {
            selectedUserType.isNullOrEmpty() -> viewDataBinding?.descLayout?.visibility = View.GONE
            else -> if (selectedUserType == Constants.TEACHER_TYPE) {
                viewDataBinding?.descLayout?.filter_tv?.text = Html.fromHtml(("<font color=#F39C12>" + "Filter:" + " </font>" +
                        "<font>" + "User: " + "</font>" +
                        "<b>" + selectedUserType?.let { getUserTypeByName(it) } + " / </b>" +
                        "<font>" + "From Date: " + "</font>" +
                        "<b>" + selectedFromDate + " - </b>" +
                        "<font>" + "To Date: " + "</font>" +
                        "<b>" + selectedTodate + "</b>"
                        )
                )
            } else {
                viewDataBinding?.descLayout?.filter_tv?.text = Html.fromHtml(("<font color=#F39C12>" + "Filter:" + " </font>" +
                        "<font>" + "User: " + "</font>" +
                        "<b>" + selectedUserType?.let { getUserTypeByName(it) } + " / </b>" +
                        "<font>" + "Class: " + "</font>" +
                        "<b>" + selectedClass?.let { getClassTypeByName(it) } + " / </b>" +
                        "<font>" + "Section: " + "</font>" +
                        "<b>" + selectedSection?.let { getSectionTypeByName(it) } + " / </b>" +
                        "<font>" + "From Date: " + "</font>" +
                        "<b>" + selectedFromDate + " - </b>" +
                        "<font>" + "To Date: " + "</font>" +
                        "<b>" + selectedTodate + "</b>"
                        )
                )
            }
        }
    }

    private fun getSectionTypeByName(sectionType: String): String? {
        when (sectionType) {
            Constants.SECTION_A -> {
                return resources.getString(R.string.a_section)
            }
            Constants.SECTION_B -> {
                return resources.getString(R.string.b_section)
            }
            Constants.SECTION_C -> {
                return resources.getString(R.string.c_section)
            }
        }
        return ""
    }

    private fun getClassTypeByName(classType: String): String? {
        when (classType) {
            Constants.PREKG -> {
                return resources.getString(R.string.preKg)
            }
            Constants.LKG -> {
                return resources.getString(R.string.lkg)
            }
            Constants.UKG -> {
                return resources.getString(R.string.ukg)
            }
        }
        return ""
    }

    private fun getUserTypeByName(type: String): String {
        when (type) {
            Constants.TEACHER_TYPE -> {
                return resources.getString(R.string.teacher)
            }
            Constants.STUDENT_TYPE -> {
                return resources.getString(R.string.student)
            }
            Constants.ADMIN_TYPE -> {
                return resources.getString(R.string.admin)
            }
        }
        return ""
    }


    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.getAttendanceReport().observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        try {
                            it.data?.data?.let { it1 -> viewModel.reportAttendanceList.addAll(it1) }
                            setUpRecyclerview(viewModel.reportAttendanceList)
                        } catch (e: Exception) {
                            Log.i("catch Error", "" + e.message)
                        }
                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                    }
                }

            }
        })
    }

    private fun setUpRecyclerview(reportAttendanceList: ArrayList<ReportAttenanceResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            R.layout.report_attendance_list_item,
            BR.reportAttendanceItem,
            reportAttendanceList,
            null,
            this
        )
        viewDataBinding?.reportRv?.adapter = baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }


    override fun onRadioButtonClick(id: Int) {
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onItemClick(view: View, position: Int, v: ReportAttendanceListItemBinding) {
    }

    override fun onDataBind(
        v: ReportAttendanceListItemBinding,
        onClickListener: View.OnClickListener
    ) {
    }

    override fun onTriggerSchoolSelection() {
        viewModel.reportAttendanceList.clear()
        viewModel.loadAttendanceReport()
    }
}
