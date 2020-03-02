package com.colan.kindercare.ui.modules.teacher.dailyActivity

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.model.ChooseActivityModel
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel

class DailyActivityVM(application: Application) : BaseViewModel<BaseNavigator>(application) {

    var activityModel = MutableLiveData<ArrayList<ChooseActivityModel>>()
    private var activityList = ArrayList<ChooseActivityModel>()
    var activitiesNavigationIndex=MutableLiveData(1)
    var activitiesType=ObservableField("")
    var isFinish = ObservableBoolean(false)
    var isLast = MutableLiveData(false)
    private var mealList = ArrayList<MealModel>()
    var studentModel = MutableLiveData<ArrayList<StudentModel>>()
    var mealModel = MutableLiveData<ArrayList<MealModel>>()
    private var studentList = ArrayList<StudentModel>()
    var isParent= ObservableField(false)
    var isteacher= ObservableField(false)
    var isAdmin= ObservableField(false)
    var isSuperAdmin= ObservableField(true)


    init {
        activityModel.value=loadActivityList()
        studentModel.value = loadStudentDetail()
        mealModel.value=loadMealData()
    }

    private fun loadActivityList(): ArrayList<ChooseActivityModel>? {
        val model1=ChooseActivityModel(1,"Photo", R.drawable.ic_choose_photo)
        val model2=ChooseActivityModel(2,"Video", R.drawable.ic_choose_video)
        val model3=ChooseActivityModel(3,"Meal", R.drawable.ic_choose_meal)
        val model4=ChooseActivityModel(4,"Nap", R.drawable.ic_choose_nap)
        val model5=ChooseActivityModel(5,"Class room", R.drawable.ic_choose_classroom)
        val model6=ChooseActivityModel(6,"Bathroom", R.drawable.ic_choose_toilet)
        val model7=ChooseActivityModel(7,"Medicine", R.drawable.ic_choose_drugs)
        val model8=ChooseActivityModel(8,"Incident", R.drawable.ic_choose_incident)
        activityList.add(model1)
        activityList.add(model2)
        activityList.add(model3)
        activityList.add(model4)
        activityList.add(model5)
        activityList.add(model6)
        activityList.add(model7)
        activityList.add(model8)
        return activityList
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

    fun onBack(){
        if(!(activitiesNavigationIndex.value!!<1)){
           activitiesNavigationIndex.value=activitiesNavigationIndex.value!!-1
        }
    }
}