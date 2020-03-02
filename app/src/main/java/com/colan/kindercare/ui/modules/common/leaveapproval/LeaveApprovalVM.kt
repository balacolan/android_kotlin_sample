package com.colan.kindercare.ui.modules.common.leaveapproval

import android.app.Application
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.LeaveApprovalModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.data.repository.leaveApproval.ILeaveApprovaControllerlRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class LeaveApprovalVM(application: Application) : BaseViewModel<IRadioListener>(application),
    KoinComponent {

    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val iLeaveApprovaControllerlRepository: ILeaveApprovaControllerlRepository by inject()
    var leaveApprovalModel = MutableLiveData<ArrayList<LeaveApprovalModel>>()
    var leaveApprovalList = ArrayList<LeaveApprovalListResponse>()
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isAdmin = ObservableField(true)
    var itemId = ObservableField("4")
    var isSuperAdmin = ObservableField(false)
    var approvedStatus = ObservableField(0)
    val approvalListResponse =
        MutableLiveData<Resource<BaseResponse<List<LeaveApprovalListResponse>>>>()
    val approvalResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val rejectResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val mShowProgressBar = SingleLiveData<Boolean>()
    var name = ObservableField("")
    var mClass = ObservableField("")
    var requestDate = ObservableField("")
    var leaveDays = ObservableField("")
    var leaveFrom = ObservableField("")
    var leaveTill = ObservableField("")
    var leaveType = ObservableField("")
    var reason = ObservableField("")
    var contacts = ObservableField("")

    var getLeaveApprovalListApiJob: Job? = null

    init {
        mShowProgressBar.value = true
        setUpUserType()

    }

    fun loadLeaveApproval(userType: String) {
        mShowProgressBar.value = true
        GlobalScope.launch {
            getLeaveApprovalListApiJob = async(Dispatchers.IO) {
                iLeaveApprovaControllerlRepository.filterLeaveApprovalList(
                    iSharedPreferenceService.getStringValue(
                        Constants.SCHOOL_ID
                    ), userType, approvalListResponse
                )
            }

        }

    }


    fun onRbChanged(button: RadioGroup, id: Int) {
        getNavigator().onRadioButtonClick(id)
    }


    private fun setUpUserType() {
        when {
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "2" -> {
                isSuperAdmin = ObservableField(true)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "3" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(true)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "4" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(true)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "5" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(true)
            }
        }
    }

    fun doCallApproveLeaveRequestAPI() {
        mShowProgressBar.value = true
        iLeaveApprovaControllerlRepository.updateLeaveRequest(
            iSharedPreferenceService.getStringValue(
                Constants.SCHOOL_ID
            ), itemId.get().toString(), Constants.APPROVED, approvalResponse
        )
    }

    fun doCallRejectLeaveRequestAPI() {
        mShowProgressBar.value = true
        iLeaveApprovaControllerlRepository.updateLeaveRequest(
            iSharedPreferenceService.getStringValue(
                Constants.SCHOOL_ID
            ), itemId.get().toString(), Constants.REJECTED, rejectResponse
        )
    }
}