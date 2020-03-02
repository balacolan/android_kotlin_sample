package com.colan.kindercare.ui.dashboard

import android.app.Application
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.SelectChildModel
import com.colan.kindercare.data.model.SelectSchoolModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.SchoolListResponse
import com.colan.kindercare.data.repository.school.ISchoolControllerRespository
import com.colan.kindercare.data.repository.user.IUserControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject

class DashboardVM (application: Application): BaseViewModel<BaseNavigator>(application),
    KoinComponent {

    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val schoolControllerRepository: ISchoolControllerRespository by inject()
    val isShowingChooseChldlayout=ObservableField(false)
    val isShowingSchoollayout=ObservableField(false)
    var selectChildModel = MutableLiveData<ArrayList<SelectChildModel>>()
    private var childList = ArrayList<SelectChildModel>()
    var selectSchoolModel = MutableLiveData<ArrayList<SelectSchoolModel>>()
    val schoolListResponse = MutableLiveData<Resource<BaseResponse<List<SchoolListResponse>>>>()
    var schoolList = ArrayList<SchoolListResponse>()
    var currentDate=ObservableField("")
    var childName=ObservableField("")
    var schoolName=ObservableField("")
    var schoolBranch=ObservableField("")
    var childSection=ObservableField("")
    var allSelected=ObservableField(false)
    var photoSelected=ObservableField(true)
    var classroomSelected=ObservableField(false)
    var videoSelected=ObservableField(false)
    var mealSelected=ObservableField(false)
    var napSelected=ObservableField(false)
    var isParent=ObservableField(false)
    var isteacher=ObservableField(false)
    var isAdmin=ObservableField(false)
    var isSuperAdmin=ObservableField(true)
    val mGenderList = ObservableArrayList<String>()
    val mClassList = ObservableArrayList<String>()
    val mSectionList = ObservableArrayList<String>()

    var getSchoolListApiJob: Job? = null

    init {
        currentDate.set(getCurrentDateTime().toString("dd MMMM yyyy"))
        setUpUserType()
        selectChildModel.value = loadListData()
        loadLSchoolListData()
    }



    private fun loadLSchoolListData(){
        GlobalScope.launch {
            getSchoolListApiJob = async(Dispatchers.IO) {
                schoolControllerRepository.getAllSchoolList(schoolListResponse)
            }
        }
    }

    private fun loadListData(): ArrayList<SelectChildModel> {
        val model1 = SelectChildModel(1,R.drawable.ic_profile_pic, "Staurt Little","PreKG, A Section",true)
        childName.set(model1.chileName)
        childSection.set(model1.chileSection)
        val model2 = SelectChildModel(2,R.drawable.ic_profile_pic, "Staurt Binny","PreKG, B Section",false)
        childList.add(model1)
        childList.add(model2)
        return this.childList
    }


    fun onClickAction(view: View){
        getNavigator().onClickView(view)
    }

    fun onAllClicked(view: View){
        allSelected.set(true)
        photoSelected.set(false)
        classroomSelected.set(false)
        videoSelected.set(false)
        mealSelected.set(false)
        napSelected.set(false)
        getNavigator().onClickView(view)
    }

    fun onPhtosClicked(view: View){
        allSelected.set(false)
        photoSelected.set(true)
        classroomSelected.set(false)
        videoSelected.set(false)
        mealSelected.set(false)
        napSelected.set(false)
        getNavigator().onClickView(view)
    }

    fun onClassroomClicked(view: View){
        allSelected.set(false)
        photoSelected.set(false)
        classroomSelected.set(true)
        videoSelected.set(false)
        mealSelected.set(false)
        napSelected.set(false)
        getNavigator().onClickView(view)
    }

    fun onMealClicked(view: View){
        allSelected.set(false)
        photoSelected.set(false)
        classroomSelected.set(false)
        videoSelected.set(false)
        mealSelected.set(true)
        napSelected.set(false)
        getNavigator().onClickView(view)
    }

    fun onNapClicked(view: View){
        allSelected.set(false)
        photoSelected.set(false)
        classroomSelected.set(false)
        videoSelected.set(false)
        mealSelected.set(false)
        napSelected.set(true)
        getNavigator().onClickView(view)
    }

    fun onVideoClicked(view: View){
        allSelected.set(false)
        photoSelected.set(false)
        videoSelected.set(true)
        classroomSelected.set(false)
        mealSelected.set(false)
        napSelected.set(false)
        getNavigator().onClickView(view)
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

}