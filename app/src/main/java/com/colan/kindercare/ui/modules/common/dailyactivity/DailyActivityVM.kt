package com.colan.kindercare.ui.modules.common.dailyactivity

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.model.DailyActivityModel
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.MessageModel
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.getCurrentDateTime
import com.colan.kindercare.utils.toString

class DailyActivityVM(application: Application) : BaseViewModel<BaseNavigator>(application) {

    var listAdvancedQuoteResult = MutableLiveData<List<DailyActivityModel>>()
    var isReject = ObservableBoolean(false)
    var studentModel = MutableLiveData<ArrayList<StudentModel>>()
    var activitiesNavigationIndex=MutableLiveData(1)
    private var studentList = ArrayList<StudentModel>()
    var fragmentNavigationIndex = MutableLiveData(1)
    var mealModel = MutableLiveData<ArrayList<MealModel>>()
    private var mealList = ArrayList<MealModel>()
    var isParent= ObservableField(false)
    var isteacher= ObservableField(false)
    var isAdmin= ObservableField(false)
    var isSuperAdmin= ObservableField(true)
    var activitiesType=ObservableField("")
    var currentDate=ObservableField("")


    init {
        val date = getCurrentDateTime()
        val dateInString = date.toString("dd MMMM yyyy")
        currentDate.set(dateInString)

        studentModel.value = loadStudentDetail()
        listAdvancedQuoteResult.value = quickAdvancedQuoteResult()
        mealModel.value=loadMealData()
    }

    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    private fun quickAdvancedQuoteResult(): ArrayList<DailyActivityModel> {
        val advancedQuoteData = ArrayList<DailyActivityModel>()

        val data1 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Pending",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data1)


        val data2 = DailyActivityModel(
            "13/11/2019",
            "Nap",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data2)


        val data3 = DailyActivityModel(
            "13/11/2019",
            "Classroom",
            "Rejected",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data3)


        val data4 = DailyActivityModel(
            "13/11/2019",
            "Medicine",
            "Pending",
            "20 Dec"
        )
        advancedQuoteData.add(data4)


        val data5 = DailyActivityModel(
            "13/11/2019",
            "Classroom",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data5)

        val data6 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Rejected",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data6)

        val data7 = DailyActivityModel(
            "13/11/2019",
            "Medicine",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data7)

        val data8 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Rejected",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data8)

        val data9 = DailyActivityModel(
            "13/11/2019",
            "Nap",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data9)

        val data10 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Rejected",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data10)

        val data11 = DailyActivityModel(
            "13/11/2019",
            "Medicine",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data11)

        val data12 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data12)

        val data13 = DailyActivityModel(
            "13/11/2019",
            "Incident",
            "Approved",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data13)

        val data14 = DailyActivityModel(
            "13/11/2019",
            "Meal",
            "Rejected",
            "LKG,  A Section"
        )
        advancedQuoteData.add(data14)

        return advancedQuoteData
    }

    private fun loadStudentDetail(): ArrayList<StudentModel>? {
        val model1 = StudentModel("Hemsworth")
        val model2 = StudentModel("Hemsworth")
        val model3 = StudentModel("Chrish")
        val model4 = StudentModel("Chrish")
        val model5 = StudentModel("Chrish")
        val model6 = StudentModel("Chrish")
        val model7 = StudentModel("Chrish")
        val model8 = StudentModel("Chrish")
        val model9 = StudentModel("Hemsworth")
        val model10 = StudentModel("Hemsworth")
        val model11 = StudentModel("Hemsworth")
        val model12 = StudentModel("Hemsworth")

        studentList.add(model1)
        studentList.add(model2)
        studentList.add(model3)
        studentList.add(model4)
        studentList.add(model5)
        studentList.add(model6)
        studentList.add(model7)
        studentList.add(model8)
        studentList.add(model9)
        studentList.add(model10)
        studentList.add(model11)
        studentList.add(model12)
        return studentList
    }

    private fun loadMealData(): ArrayList<MealModel>? {
        val model1 = MealModel(1, "AM Snacks","Coffee and Biscuits")
        val model2 = MealModel(2, "Lunch","Sandwich and Fruits,Milk and Banana")
        mealList.add(model1)
        mealList.add(model2)
        return mealList
    }
}