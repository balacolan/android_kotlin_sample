package com.colan.kindercare.ui.modules.admin.pickupApproval

import android.app.Application
import android.view.View
import android.widget.RadioGroup
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.data.model.EnrollmentModel
import com.colan.kindercare.data.model.PickUpPersonModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.data.remote.data.response.pickupPerson.PickupPersonListResponse
import com.colan.kindercare.data.repository.attendance.IAttendanceControllerRepository
import com.colan.kindercare.data.repository.pickupPersonal.IPickupUpPersonControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PickupPersonVM(application: Application) : BaseViewModel<BaseNavigator>(application),KoinComponent {

    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val pickupUpPersonControllerRepository: IPickupUpPersonControllerRepository by inject()
    var currentDate = ObservableField("")
    var enrollmentModel = MutableLiveData<ArrayList<EnrollmentModel>>()
    private var enrollmentList = ArrayList<EnrollmentModel>()
    var teacherSelected = ObservableField(true)
    var studentSelected = ObservableField(true)
    var pickupApprovalModel = MutableLiveData<ArrayList<PickUpPersonModel>>()
    var pickupApprovalList = ArrayList<PickupPersonListResponse>()

    var isParent=ObservableField(false)
    var isteacher=ObservableField(false)
    var isAdmin=ObservableField(true)
    var isSuperAdmin=ObservableField(false)

    val mShowProgressBar = SingleLiveData<Boolean>()
    var getPickupPersonListApiJob: Job? = null
    val pickupListResponse = MutableLiveData<Resource<BaseResponse<List<PickupPersonListResponse>>>>()
    val statusResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var itemId = ObservableField("")
    var studentName = ObservableField("")
    var approvedStatus = ObservableField("")
    var classSection = ObservableField("")
    var fathersName = ObservableField("")
    var mothersName = ObservableField("")
    var pickupPerson = ObservableField("")
    var relationShip = ObservableField("")
    var contacts = ObservableField("")

    init {
        mShowProgressBar.value=true
        setUpUserType()
        currentDate.set(getCurrentDateTime().toString("dd MMMM yyyy"))
        loadPickupApprovalData()
    }

     fun loadPickupApprovalData(){
        GlobalScope.launch {
            getPickupPersonListApiJob = async(Dispatchers.IO) {
                pickupUpPersonControllerRepository.getPickupPersonList(iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),pickupListResponse)
            }
        }

    }


    fun onClickAction(view: View) {
        getNavigator().onClickView(view)
    }

    fun onRbChanged(button: RadioGroup, id: Int) {
        // getNavigator().onRadioButtonClick(id)
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

    fun doCallApproveRejectApi(status: String) {
        mShowProgressBar.value=true
        pickupUpPersonControllerRepository.statusChangePickupRequest(itemId.get().toString(),"PUT",status,statusResponse)
    }
}