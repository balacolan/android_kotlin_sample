package com.colan.kindercare.ui.modules.common.report

import android.app.Application
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.AttendanceReportModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.report.ReportAttenanceResponse
import com.colan.kindercare.data.remote.data.response.report.ReportSalaryResponse
import com.colan.kindercare.data.repository.report.IReportControllerRepository
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject

class ReportVM(application: Application) : BaseViewModel<IRadioListener>(application),
    KoinComponent {


    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val reportControllerRepository: IReportControllerRepository by inject()
    val attendendaceSelected = ObservableField<Boolean>(true)
    val salarySelected = ObservableField<Boolean>(true)
    var reportModel = MutableLiveData<ArrayList<AttendanceReportModel>>()
    private var attendanceReportList = ArrayList<AttendanceReportModel>()
    val mShowProgressBar = SingleLiveData<Boolean>()
    val getReportSalaryListResponse =
        MutableLiveData<Resource<BaseResponse<ArrayList<ReportSalaryResponse>>>>()
    private val getReportAttendanceListResponse =
        MutableLiveData<Resource<BaseResponse<ArrayList<ReportAttenanceResponse>>>>()


    var teacherSelected = ObservableField(true)
    var adminSelected = ObservableField(false)
    var superadminSelected = ObservableField(true)

    var mAttendanceFilterValues = ObservableField("")
    var mFromDate = ObservableField<String>("")
    var mToDate= ObservableField<String>("")
    var mUserType = ObservableField<String>("")
    var mClassType = ObservableField<String>("")
    var mSectionType = ObservableField<String>("")


    var reportSalaryModel = MutableLiveData<ArrayList<AttendanceReportModel>>()
    private var attendanceSalayReportList = ArrayList<AttendanceReportModel>()
    var reportSalaryList = ArrayList<ReportSalaryResponse>()
    var reportAttendanceList = ArrayList<ReportAttenanceResponse>()
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isAdmin = ObservableField(false)
    var isSuperAdmin = ObservableField(true)

    fun onRbChanged(button: RadioGroup, id: Int) {
        getNavigator().onRadioButtonClick(id)
    }

    init {
        setUpUserType()
        setPermissionTpe()
    }


    fun loadAttendanceReport() {
        mShowProgressBar.value = true
        reportControllerRepository.getReportAttendance(
            mFromDate.get().toString(),
            mToDate.get().toString(),
            mUserType.get().toString(),
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            mClassType.get().toString(),
            mSectionType.get().toString(),
            getReportAttendanceListResponse

        )
    }

    fun getAttendanceReport(): MutableLiveData<Resource<BaseResponse<ArrayList<ReportAttenanceResponse>>>> {
        return getReportAttendanceListResponse
    }

    fun loadSalaryReport() {
        mShowProgressBar.value = true
        reportControllerRepository.getReportSalary(
            mFromDate.get().toString(),
            mToDate.get().toString(),
            mUserType.get().toString(),
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            getReportSalaryListResponse

        )
    }

    private fun setUpUserType() {
        when {
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID)=="2" -> {
                isSuperAdmin=ObservableField(true)
                isAdmin=ObservableField(false)
                isteacher=ObservableField(false)
                isParent=ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID)=="3" -> {
                isSuperAdmin=ObservableField(false)
                isAdmin=ObservableField(true)
                isteacher=ObservableField(false)
                isParent=ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID)=="4" -> {
                isSuperAdmin=ObservableField(false)
                isAdmin=ObservableField(false)
                isteacher=ObservableField(true)
                isParent=ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID)=="5" -> {
                isSuperAdmin=ObservableField(false)
                isAdmin=ObservableField(false)
                isteacher=ObservableField(false)
                isParent=ObservableField(true)
            }
        }
    }

    private fun setPermissionTpe() {
        when {
            iSharedPreferenceService.getStringValue(Constants.VIEW_SALARY) == "0" -> adminSelected.set(
                true
            )
            iSharedPreferenceService.getStringValue(Constants.VIEW_SALARY) == "1" -> adminSelected.set(
                false
            )
        }
    }
}