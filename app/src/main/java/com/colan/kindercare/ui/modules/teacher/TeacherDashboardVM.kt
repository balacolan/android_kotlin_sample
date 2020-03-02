package com.colan.kindercare.ui.modules.teacher

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.model.AttendanceModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString

class TeacherDashboardVM(application: Application) : BaseViewModel<BaseNavigator>(application) {


    var currentDate= ObservableField("")
    var attendanceModel = MutableLiveData<ArrayList<AttendanceModel>>()
    private var attendanceList = ArrayList<AttendanceModel>()

    init {
        currentDate.set(getCurrentDateTime().toString("dd MMMM yyyy"))
        attendanceModel.value=loadAttedanceData()
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

    fun onClickAction(view: View){
        getNavigator().onClickView(view)
    }

}