package com.colan.kindercare.ui.modules.admin

import android.app.Application
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.data.model.EnrollmentModel
import com.colan.kindercare.data.model.PickUpPersonModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString
import org.koin.core.KoinComponent
import org.koin.core.inject

class AdminDashboardVM(application: Application) : BaseViewModel<BaseNavigator>(application),
    KoinComponent {
    val iSharedPreferenceService: ISharedPreferenceService by inject()
    var currentDate = ObservableField("")
    var enrollmentModel = MutableLiveData<ArrayList<EnrollmentModel>>()
    private var enrollmentList = ArrayList<EnrollmentModel>()
    var teacherSelected = ObservableField(true)
    var studentSelected = ObservableField(true)
    var pickupApprovalModel = MutableLiveData<ArrayList<PickUpPersonModel>>()
    private var pickupApprovalList = ArrayList<PickUpPersonModel>()
    var isParent=ObservableField(false)
    var isteacher=ObservableField(false)
    var isAdmin=ObservableField(false)
    var isSuperAdmin=ObservableField(true)

    var attendanceModel = MutableLiveData<ArrayList<AttendanceModel>>()
    private var attendanceList = ArrayList<AttendanceModel>()

    init {
        currentDate.set(getCurrentDateTime().toString("dd MMMM yyyy"))
        enrollmentModel.value = loadEnrollmentData()
        pickupApprovalModel.value = loadPickupApprovalData()
        attendanceModel.value = loadAttedanceData()
        setUpUserType()
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

    fun onClickAction(view: View) {
        getNavigator().onClickView(view)
    }

    fun onRbChanged(button: RadioGroup, id: Int) {
        // getNavigator().onRadioButtonClick(id)
    }

}