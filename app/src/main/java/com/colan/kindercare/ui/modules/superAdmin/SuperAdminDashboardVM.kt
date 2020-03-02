package com.colan.kindercare.ui.modules.superAdmin

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.dashboard.DashboardResponse
import com.colan.kindercare.data.repository.user.IUserControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

class SuperAdminDashboardVM  (application: Application) : BaseViewModel<BaseNavigator>(application),KoinComponent {

    private val dashboardControllerRepository: IUserControllerRepository by inject()
    val iSharedPreferenceService: ISharedPreferenceService by inject()
    val response = MutableLiveData<Resource<BaseResponse<List<DashboardResponse>>>>()

    var noOfStutents=ObservableField("")
    var noOfAdmin=ObservableField("")
    var noOfTeachers=ObservableField("")
    var noOfStaffs=ObservableField("")

    var isSuperAdmin= ObservableField(true)

    init {
        loadDashBoardData()
    }

    fun loadDashBoardData() {
        dashboardControllerRepository.dashboard(iSharedPreferenceService.getStringValue(Constants.INSTITUTE_ID),iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID) ,response)
    }


}