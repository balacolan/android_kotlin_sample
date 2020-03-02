package com.colan.kindercare.ui.modules.parent.calender

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.model.CalenderHolydayModel
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel

class CalenderVM (application: Application): BaseViewModel<BaseNavigator>(application) {

    var holidayModel = MutableLiveData<ArrayList<CalenderHolydayModel>>()
    private var holidayList = ArrayList<CalenderHolydayModel>()

    init {
        holidayModel.value=loadMealData()
    }

    private fun loadMealData(): ArrayList<CalenderHolydayModel>? {
        val model1 = CalenderHolydayModel(1, "WED","4","Telugu New Year Day")
        val model2 = CalenderHolydayModel(2, "THU","5","Tamil New Year Day")
        val model3 = CalenderHolydayModel(3, "FRI","6","Mahaveer Jayanthi")
        val model4 = CalenderHolydayModel(4, "MON","9"," Good Friday")
        holidayList.add(model1)
        holidayList.add(model2)
        holidayList.add(model3)
        holidayList.add(model4)
        return holidayList
    }
}