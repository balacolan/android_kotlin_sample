package com.colan.kindercare.ui.modules.common.leavestatus

import android.app.Application
import android.view.View
import android.widget.AdapterView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.LeaveRequestModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApplication.LeaveApplicationListResponse
import com.colan.kindercare.data.repository.leaveApplication.ILeaveApplicationControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject

class LeaveStatusVM(application: Application) : BaseViewModel<BaseNavigator>(application),
    KoinComponent {


    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val iLeaveApplicationControllerRepository: ILeaveApplicationControllerRepository by inject()
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isAdmin = ObservableField(false)
    var isSuperAdmin = ObservableField(true)
    var datePicker = ObservableField("")
    val mShowProgressBar = SingleLiveData<Boolean>()

    var mContact = ObservableField("")
    var mLeaveType = ObservableField("")
    var mFromDate = ObservableField("")
    var mToDate = ObservableField("")
    var mReason = ObservableField("")
    var mLeaveDays = ObservableField("")
    var mLeaveApproveStatus=ObservableField("")


    var mContactNo = ObservableField("")
    var mLeaveTypeData = ObservableField("")
    var mFromDateData = ObservableField("")
    var mToDateData = ObservableField("")
    var mReasonData = ObservableField("")
    var mLeaveDaysData = ObservableField("")
    var mLeaveApproveStatusData=ObservableField("")


    var listAdvancedQuoteResult = MutableLiveData<List<LeaveRequestModel>>()
    var addLeaveApplicationResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var deleteLeaveApplicationData = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val getLeaveFilterListResponse =
        MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>()
    val getLeaveSelectedIdResponse = MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>()
    var getLeaveFilterResult= ArrayList<LeaveApplicationListResponse>()
    var getLeaveItemIdData= ArrayList<LeaveApplicationListResponse>()

    init {
        //listAdvancedQuoteResult.value = quickAdvancedQuoteResult()

    }

    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    fun loadLeaveApplicationListData() {
        mShowProgressBar.value = true
        iLeaveApplicationControllerRepository.getLeaveApplicaionList(
            getLeaveFilterListResponse
        )
    }

    fun loadLeavesList() {
        mShowProgressBar.value=true
        iLeaveApplicationControllerRepository.getLeaveFilterData(
            mFromDate.get().toString(),
            mToDate.get().toString(),
            mLeaveType.get().toString(),
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            getLeaveFilterListResponse

        )
    }

    fun loadItemIdData() {
        mShowProgressBar.value=true
        iLeaveApplicationControllerRepository.getLeaveItemIdData(
            iSharedPreferenceService.getStringValue(Constants.CARD_ITEM_ID),
            getLeaveSelectedIdResponse

        )
    }

    fun loadItemIdDelete() {
        mShowProgressBar.value=true
        iLeaveApplicationControllerRepository.getLeaveItemIdDelete(
            iSharedPreferenceService.getStringValue(Constants.CARD_ITEM_ID),
            deleteLeaveApplicationData

        )
    }

    fun doCallApproveLeaveRequestAPI() {
        if (validateCredentials()) {
            mShowProgressBar.value = true
            iLeaveApplicationControllerRepository.addLeaveApplication(
                iSharedPreferenceService.getStringValue(
                    Constants.SCHOOL_ID
                ),
                //mLeaveDays.get().toString(),
                mFromDate.get().toString(),
                mToDate.get().toString(),
                mLeaveType.get().toString(),
                mReason.get().toString(),
                mContact.get().toString(),
                addLeaveApplicationResponse

            )
        }
    }

    private fun validateCredentials(): Boolean {
        when{
            //mLeaveDays.get()!!.isEmpty() -> putToast("Please select Leave days")
            mFromDate.get()!!.isEmpty() -> putToast("Please enter From Date")
            mToDate.get()!!.isEmpty() -> putToast("Please enter To Date")
            mLeaveType.get()!!.isEmpty() -> putToast("Please Select Leave Type")
            mReason.get()!!.isEmpty() -> putToast("Please enter Reason")
            mContact.get()!!.isEmpty() -> putToast("Please enter Contact Number")
            else -> return true
        }
        return false
    }

    fun onSelectLeaveDays(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        mLeaveDays.set(pos.toString())
    }

    fun onSelectLeaveType(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        mLeaveType.set((pos + 1).toString())
    }


    private fun quickAdvancedQuoteResult(): ArrayList<LeaveRequestModel> {
        val advancedQuoteData = ArrayList<LeaveRequestModel>()

        val data1 = LeaveRequestModel(
            R.drawable.ic_medical_leave,
            "SickLeave",
            "10 Days",
            "Pending",
            "20 Dec",
            "26 Dec"
        )
        advancedQuoteData.add(data1)


        val data2 = LeaveRequestModel(
            R.drawable.ic_annual_leave,
            "Annual Leave",
            "10 Days",
            "Approved",
            "26 Dec",
            "26 Dec"
        )
        advancedQuoteData.add(data2)


        val data3 = LeaveRequestModel(
            R.drawable.ic_medical_leave,
            "SickLeave",
            "10 Days",
            "Rejected",
            "26 Dec",
            "26 Dec"
        )
        advancedQuoteData.add(data3)


        val data4 = LeaveRequestModel(
            R.drawable.ic_other_leave,
            "Others",
            "10 Days",
            "Pending",
            "20 Dec",
            "26 Dec"
        )
        advancedQuoteData.add(data4)


        val data5 = LeaveRequestModel(
            R.drawable.ic_annual_leave,
            "Annual Leave",
            "10 Days",
            "Approved",
            "26 Dec",
            "26 Dec"
        )
        advancedQuoteData.add(data5)

        return advancedQuoteData
    }
}