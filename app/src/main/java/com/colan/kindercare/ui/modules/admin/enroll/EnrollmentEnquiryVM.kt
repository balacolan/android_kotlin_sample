package com.colan.kindercare.ui.modules.admin.enroll

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.data.model.EnrollmentModel
import com.colan.kindercare.data.model.PickUpPersonModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollmentListResponse
import com.colan.kindercare.data.repository.enrolment_enquiry.IEnrolmentControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.*
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject


class EnrollmentEnquiryVM (application: Application) : BaseViewModel<BaseNavigator>(application),
    KoinComponent {
    private val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val iEnrolmentRepository: IEnrolmentControllerRepository by inject()
    var currentDate = ObservableField("")
    var selectedDate = ObservableField("")
    var enrollmentModel = MutableLiveData<Resource<BaseResponse<EnrollmentListResponse>>>()
    var deleteListResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    private var enrollmentList = ArrayList<EnrollmentModel>()
    var teacherSelected = ObservableField(true)
    var studentSelected = ObservableField(true)
    var pickupApprovalModel = MutableLiveData<ArrayList<PickUpPersonModel>>()
    private var pickupApprovalList = ArrayList<PickUpPersonModel>()
    var isParent=ObservableField(false)
    var isteacher=ObservableField(false)
    var isAdmin=ObservableField(true)
    var isSuperAdmin=ObservableField(false)
    var attendanceModel = MutableLiveData<ArrayList<AttendanceModel>>()
    private var attendanceList = ArrayList<AttendanceModel>()
    var addEnrollResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var spinnerArrayListClass = MutableLiveData<Resource<BaseResponse<ArrayList<EnrollClassResponse>>>>()
    var addEnrollJob: Job? = null
    var mClassSpinnerList = ObservableArrayList<String>()

    var childName=ObservableField("")
    var childID=ObservableField("")
    var childAge=ObservableField("")
    var childDob=ObservableField("")
    var childParentName=ObservableField("")
    var childParentEmail=ObservableField("")
    var childParentMobile=ObservableField("")
    var childStatus=ObservableField("")
    var childPurpose=ObservableField("")
    var childMotherName=ObservableField("")
    var childClass=ObservableField("")
    var childTotalCount=ObservableField("")
    var childAccepted=ObservableField("")
    var childRejected=ObservableField("")
    var getListJob: Job? = null
    var getDeleteJob: Job? = null


    var setInstituteId=ObservableField("")
    var setSchoolId=ObservableField("")
    var setStudentName=ObservableField("")
    var setAge=ObservableField("")
    var setDob=ObservableField("")
    var setClass=ObservableField("")
    var setFatherName=ObservableField("")
    var setMotherName=ObservableField("")
    var setContact=ObservableField("")
    var setEmail=ObservableField("")
    var setPurpose=ObservableField("")
    var setStatus=ObservableField("")
    var mClassSpinnerDataList = ObservableArrayList<EnrollClassResponse>()



    val mShowProgressBar = SingleLiveData<Boolean>()
    init {
        currentDate.set(dateToStringCustom(getCurrentDateTime().toString("dd MMMM yyyy")))
        selectedDate.set(getCurrentDateTime().toString("dd MMMM yyyy"))
        pickupApprovalModel.value = loadPickupApprovalData()
        attendanceModel.value = loadAttedanceData()
        setUpUserType()
      //  addEnrollApi()

    }

    fun loadEnrollClass() {
        iEnrolmentRepository.getEnrollClass(
            spinnerArrayListClass
        )
    }

      fun getEnrolmentList(date: String?) {
          mShowProgressBar.value = true
          GlobalScope.launch{
              getListJob=async(Dispatchers.IO) {
                  iEnrolmentRepository.getEnrollmentList(iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),date,date,enrollmentModel)
              }
          }
    }

    fun deleteEnroll(){
        mShowProgressBar.value = true
        GlobalScope.launch{
            getDeleteJob=async(Dispatchers.IO) {
                iEnrolmentRepository.getDeleteEnroll(childID.get()!!,deleteListResponse)
            }
        }
    }


    fun addEnrollApi() {
        if (validateCredentials()) {
            mShowProgressBar.value = true
            iEnrolmentRepository.addEnrollmentList(
                iSharedPreferenceService.getStringValue(Constants.INSTITUTE_ID),
                iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),
                setStudentName.get().toString(),
                setAge.get().toString(),
                setDob.get().toString(),
                setClass.get().toString(),
                setFatherName.get().toString(),
                setMotherName.get().toString(),
                setContact.get().toString(),
                setEmail.get().toString(),
                setPurpose.get().toString(),
                setStatus.get().toString(),
                addEnrollResponse
            )
        }
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

    private fun loadEnrollmentData(): ArrayList<EnrollmentModel>? {
        val model1 = EnrollmentModel(1, "Shawn Michael", "Pending")
        val model2 = EnrollmentModel(2, "Chris Hemsworth ", "Accepted")
        val model3 = EnrollmentModel(3, "Robert Downey Jr.", "Rejected")
        val model4 = EnrollmentModel(4, "Tony Starck", "Pending")
        val model5 = EnrollmentModel(5, "George Bailey", "Rejected")
        val model6 = EnrollmentModel(6, " Chris Evans", "Accepted")
        enrollmentList.add(model1)
        enrollmentList.add(model2)
        enrollmentList.add(model3)
        enrollmentList.add(model4)
        enrollmentList.add(model5)
        enrollmentList.add(model6)
        return enrollmentList
    }

    private fun loadPickupApprovalData(): ArrayList<PickUpPersonModel>? {
        val model1 =
            PickUpPersonModel(1, 0, "Shawn Michael", "LKG, A Section", "10.25 AM", "Rober Downey")
        val model2 = PickUpPersonModel(
            2,
            0,
            "Chris Hemsworth ",
            "LKG, A Section",
            "10.25 AM",
            "Chris Hemsworth"
        )
        val model3 = PickUpPersonModel(
            3,
            0,
            "Robert Downey Jr.",
            "LKG, A Section",
            "10.25 AM",
            "Chris Hemsworth"
        )
        val model4 =
            PickUpPersonModel(4, 0, "Tony Starck", "LKG, A Section", "10.25 AM", "Chris Hemsworth")
        val model5 = PickUpPersonModel(
            5,
            0,
            "George Bailey",
            "LKG, A Section",
            "10.25 AM",
            "Chris Hemsworth"
        )
        val model6 =
            PickUpPersonModel(6, 0, " Chris Evans", "LKG, A Section", "10.25 AM", "Chris Hemsworth")
        pickupApprovalList.add(model1)
        pickupApprovalList.add(model2)
        pickupApprovalList.add(model3)
        pickupApprovalList.add(model4)
        pickupApprovalList.add(model5)
        pickupApprovalList.add(model6)
        return pickupApprovalList
    }


    private fun loadAttedanceData(): ArrayList<AttendanceModel>? {
        val model1 = AttendanceModel(1, "Shawn Michael", isPresent = true, isSelected = true)
        val model2 = AttendanceModel(2, "Chris Hemsworth ", isPresent = false, isSelected = false)
        val model3 = AttendanceModel(3, "Robert Downey Jr.", isPresent = true, isSelected = true)
        val model4 = AttendanceModel(4, "Tony Starck", isPresent = false, isSelected = false)
        val model5 = AttendanceModel(5, "George Bailey", isPresent = true, isSelected = true)
        val model6 = AttendanceModel(6, " Chris Evans", isPresent = false, isSelected = false)
        attendanceList.add(model1)
        attendanceList.add(model2)
        attendanceList.add(model3)
        attendanceList.add(model4)
        attendanceList.add(model5)
        attendanceList.add(model6)
        return attendanceList
    }

    private fun validateCredentials(): Boolean {
        when{
            setStudentName.get()!!.isEmpty() -> putToast("Please enter Student name")
            setAge.get()!!.isEmpty() -> putToast("Please enter child Age")
            setDob.get()!!.isEmpty() -> putToast("Please enter Date Of Birth")
            setEmail.get()!!.isEmpty() -> putToast("Please enter your Email Id")
            !validateEmail(setEmail.get()!!) -> {
                putToast("Please enter your Valid Mobile Number")
            }
            !validateMobile(setContact.get()!!) -> {
                putToast("Please enter your Valid Mobile Number")
            }
            setFatherName.get()!!.isEmpty() -> putToast("Please enter Father Name")
            setMotherName.get()!!.isEmpty() -> putToast("Please enter Mother Name")
            setContact.get()!!.isEmpty() -> putToast("Please enter Contact Number")
            else -> return true
        }
        return false
    }

    fun onClickAction(view: View) {
        getNavigator().onClickView(view)
    }

    fun onSelectItem(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        setClass.set(id.toString())
        setPurpose.set(parent?.selectedItem.toString())
        setStatus.set(pos.toString())
    }


    fun onSelectClass(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        setClass.set(mClassSpinnerDataList.get(id.toInt()).id.toString())
    }

    fun onSelectPurpose(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        setPurpose.set(parent?.selectedItem.toString())
    }

    fun onSelectStatus(
        parent: AdapterView<*>?,
        view: View?,
        pos: Int,
        id: Long
    ) {
        setStatus.set(pos.toString())
    }


    fun onRbChanged(button: RadioGroup, id: Int) {
        // getNavigator().onRadioButtonClick(id)
    }
}
