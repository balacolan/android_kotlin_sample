package com.colan.kindercare.ui.modules.admin.attendance

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceSectionResponse
import com.colan.kindercare.data.repository.attendance.IAttendanceControllerRepository
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString
import org.koin.core.KoinComponent
import org.koin.core.inject

class AdminAttendanceVM(application: Application) : BaseViewModel<IRadioListener>(application),
    KoinComponent {

    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val attendanceControllerRepository: IAttendanceControllerRepository by inject()
    var currentDate = MutableLiveData("")
    var teacherSelected = ObservableField(true)
    var adminSelected = ObservableField(false)
    var studentSelected = ObservableField(false)
    val mShowProgressBar = SingleLiveData<Boolean>()

    var mClassSpinnerList = ObservableArrayList<String>()
    var mSectionSpinnerList = ObservableArrayList<String>()
    var mSelectedClassId = ObservableField<String>()
    var mSelectedSectionId = ObservableField<String>()

    var getClassListResponse =
        MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceClassResponse>>>>()
    var getSectionListResponse =
        MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceSectionResponse>>>>()
    val getAttendanceListResponse =
        MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>()
    val getStudentAttendanceListResponse =
        MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>()
    val addAdminAttendanceResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val addTeacherAttendanceResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val addStudenceAttendanceResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var attendanceList = ArrayList<AttendanceResponse>()
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isAdmin = ObservableField(false)
    var isSuperAdmin = ObservableField(true)
    var isAdminLogin = ObservableField(true)
    var teacherSignIn = ObservableField(true)
    var studentSignIn = ObservableField(true)
    var todayDate = ObservableField("")
    var signInTime = ObservableField("")
    var signOutTime = ObservableField("")

    var mEditStaffAttendance = ObservableField(false)

    init {
        setUpUserType()
        setPermissionTpe()
        val dateInString = getCurrentDateTime().toString("dd MMMM yyyy")
        todayDate.set(getCurrentDateTime().toString("yyyy-MM-dd"))
        Log.d("todayDate", "" + todayDate.get().toString())
        currentDate.value = dateInString
        iSharedPreferenceService.setStringValue(Constants.SELECTED_DATE,getCurrentDateTime().toString("yyyy-MM-dd"))

    }

    private fun setPermissionTpe() {
        when {
            iSharedPreferenceService.getStringValue(Constants.ADMIN_LOGIN) == "1" -> isAdminLogin.set(
                true
            )
            iSharedPreferenceService.getStringValue(Constants.ADMIN_LOGIN) == "0" -> isAdminLogin.set(
                false
            )
            iSharedPreferenceService.getStringValue(Constants.TEACHER_SIGN_IN) == "1" -> teacherSignIn.set(
                false
            )
            iSharedPreferenceService.getStringValue(Constants.TEACHER_SIGN_IN) == "0" -> teacherSignIn.set(
                true
            )
            iSharedPreferenceService.getStringValue(Constants.STUDENT_SIGN_IN) == "1" -> studentSignIn.set(
                false
            )
            iSharedPreferenceService.getStringValue(Constants.STUDENT_SIGN_IN) == "0" -> studentSignIn.set(
                true
            )
            iSharedPreferenceService.getStringValue(Constants.EDIT_STAFF_ATTENDANCE) == "1" -> mEditStaffAttendance.set(
                false
            )
            iSharedPreferenceService.getStringValue(Constants.EDIT_STAFF_ATTENDANCE) == "0" -> mEditStaffAttendance.set(
                true
            )
        }
    }


    fun loadAttendanceClass() {
        //mShowProgressBar.value = true
        attendanceControllerRepository.getAttendanceClass(
            getClassListResponse
        )
    }

    fun loadAttendanceSection(classId: String) {
        //mShowProgressBar.value = true
        attendanceControllerRepository.getAttendanceSection(
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            //iSharedPreferenceService.getStringValue(Constants.CLASS),
            classId,
            getSectionListResponse
        )
    }

    fun loadAttedanceData(type: String) {
        attendanceControllerRepository.getAttendanceList(
            iSharedPreferenceService.getStringValue(Constants.SELECTED_DATE),
            type,
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            getAttendanceListResponse
        )

    }

    fun loadStudentAttedanceData(type: String) {
        // mShowProgressBar.value=true
        attendanceControllerRepository.getStudentAttendanceList(
            type,
            iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
            iSharedPreferenceService.getStringValue(Constants.SECTION),
            iSharedPreferenceService.getStringValue(Constants.CLASS),
            getStudentAttendanceListResponse
        )

    }

    fun onSelectItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        mSelectedClassId.set(getClassListResponse.value?.data?.data?.get(pos)!!.id!!.toString())
        // mSelectedClassId.set(getClassListResponse.value?.data?.data?.get(pos)!!.id!!.toInt())
        loadAttendanceSection(getClassListResponse.value?.data?.data?.get(pos)!!.id!!.toString())
        loadStudentAttedanceData(Constants.STUDENT_TYPE)
    }


    fun onSelectSectionItem(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        mSelectedSectionId.set(getSectionListResponse.value?.data?.data?.get(pos)!!.sectionId!!.toString())

    }


    private fun <T> ObservableArrayList<T>.set(index: Int) {

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
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "6" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(true)
            }
        }
    }

    fun doCallAddAdminAttendanceAPI(
        userType: String,
        schoolId: String,
        logType: String,
        selectedUserIdList: ArrayList<String>
    ) {
        attendanceControllerRepository.AddAdminAttendance(
            iSharedPreferenceService.getStringValue(Constants.SELECTED_DATE),
            userType,
            schoolId,
            logType,
            selectedUserIdList,
            signInTime.get().toString(),
            addAdminAttendanceResponse
        )
    }

    fun doCallAddTeacherAttendanceAPI(
        userType: String,
        schoolId: String,
        logType: String,
        selectedUserIdList: ArrayList<String>
    ) {
        attendanceControllerRepository.AddTeacherAttendance(
            iSharedPreferenceService.getStringValue(Constants.SELECTED_DATE),
            userType,
            schoolId,
            logType,
            selectedUserIdList,
            signInTime.get().toString(),
            addTeacherAttendanceResponse
        )
    }

    fun doCallAddStudentAttendanceAPI(
        userType: String,
        schoolId: String,
        logType: String,
        classId: String,
        sectionId: String,
        selectedUserIdList: ArrayList<String>
    ) {
        attendanceControllerRepository.AddStudentAttendance(
            iSharedPreferenceService.getStringValue(Constants.SELECTED_DATE),
            userType,
            schoolId,
            classId,
            sectionId,
            logType,
            selectedUserIdList,
            signInTime.get().toString(),
            addStudenceAttendanceResponse
        )
    }


}


