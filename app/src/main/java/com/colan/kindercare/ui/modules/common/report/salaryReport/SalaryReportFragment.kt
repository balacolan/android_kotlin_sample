package com.colan.kindercare.ui.modules.common.report.salaryReport


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
import com.colan.kindercare.data.remote.data.response.report.ReportSalaryResponse
import com.colan.kindercare.databinding.FragmentSalaryReportBinding
import com.colan.kindercare.databinding.ReportSalaryListItemBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.modules.common.report.ReportVM
import com.colan.kindercare.ui.modules.common.report.reportFilterBottomSheet.ReportSalaryFilterBottomSheet
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SchoolSelectionBroadCast
import com.colan.kindercare.utils.SchoolSelectionListner
import kotlinx.android.synthetic.main.filter_content_detail_layout.view.*


/**
 * A simple [Fragment] subclass.
 */
class SalaryReportFragment : BaseFragment<FragmentSalaryReportBinding, ReportVM>(),
    IRadioListener,
    ReportSalaryFilterBottomSheet.OnSalaryFilterApplyListner,
    OnDataBindCallback<ReportSalaryListItemBinding>, SchoolSelectionListner {

    var selectedFromDate: String? = ""
    var selectedTodate: String? = ""
    var selectedUserType: String? = ""
    var selectedClass: String? = ""
    var selectedSection: String? = ""

    override fun getSalaryFilterAppliedDate(fromDate: String, toDate: String, userType: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var baseRecyclerAdapter: BaseRecyclerViewAdapter<ReportSalaryResponse, ReportSalaryListItemBinding>? =
        null

    override val bindingVariable: Int
        get() = BR.reportVM

    override val layoutId: Int
        get() = com.colan.kindercare.R.layout.fragment_salary_report
    override val viewModel: ReportVM
        get() = ViewModelProviders.of(this).get(ReportVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        // setUpRecyclerview()
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

        viewModel.loadSalaryReport()
    }

    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        viewModel.getReportSalaryListResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        viewModel.mShowProgressBar.value = false
                        try {
                            it.data?.data?.let { it1 -> viewModel.reportSalaryList.addAll(it1) }
                            setUpRecyclerview(viewModel.reportSalaryList)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewDataBinding?.descLayout?.filter_tv?.setText(Html.fromHtml(getString(com.colan.kindercare.R.string.filter_text_salary)))

        selectedFilterText()


    }

    private fun selectedFilterText() {
        if (selectedUserType == null) {
            viewDataBinding?.descLayout?.visibility = View.GONE
        } else {

            viewDataBinding?.descLayout?.filter_tv?.setText(
                Html.fromHtml(("<font color=#F39C12>" + "Filter:" + " </font>" +
                        "<font>" + "User: " + "</font>" +
                        "<b>" + selectedUserType?.let { getUserTypeByName(it) } + " / </b>" +
                        "<font>" + "From Date: " + "</font>" +
                        "<b>" + selectedFromDate + " - </b>" +
                        "<font>" + "To Date: " + "</font>" +
                        "<b>" + selectedTodate + "</b>"
                        )
                ))

        }
    }

    private fun getUserTypeByName(type: String): String {
        when (type) {
            Constants.TEACHER_TYPE -> {
                return resources.getString(R.string.teacher)
            }
            Constants.ALL -> {
                return resources.getString(R.string.all)
            }
            Constants.OTHERS -> {
                return resources.getString(R.string.other)
            }
        }
        return ""
    }

    private fun setUpRecyclerview(reportSalaryList: ArrayList<ReportSalaryResponse>) {
        baseRecyclerAdapter = BaseRecyclerViewAdapter(
            com.colan.kindercare.R.layout.report_salary_list_item,
            BR.reportItem,
            reportSalaryList,
            null,
            this
        )
        viewDataBinding?.reportSalaryRv?.adapter = baseRecyclerAdapter
        baseRecyclerAdapter?.notifyDataSetChanged()
    }


    override fun onRadioButtonClick(id: Int) {
    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onItemClick(view: View, position: Int, v: ReportSalaryListItemBinding) {
    }

    override fun onDataBind(v: ReportSalaryListItemBinding, onClickListener: View.OnClickListener) {
    }

    override fun onTriggerSchoolSelection() {
        viewModel.reportSalaryList.clear()
        viewModel.loadSalaryReport()
    }
}
