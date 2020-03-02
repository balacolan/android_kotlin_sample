package com.colan.kindercare.ui.modules.admin.addWeeklyMenu

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.StudentModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel

class AddWeeklyMenuVM(application: Application) : BaseViewModel<BaseNavigator>(application) {

    var fragmentNavigationIndex = MutableLiveData(1)
    private var studentList = ArrayList<StudentModel>()
    private var mealList = ArrayList<MealModel>()
    var studentModel = MutableLiveData<ArrayList<StudentModel>>()
    var mealModel = MutableLiveData<ArrayList<MealModel>>()
    var isFinish = ObservableBoolean(false)
    var isBack = ObservableBoolean(false)

    init {
        studentModel.value = loadStudentDetail()
        mealModel.value=loadMealData()
    }

    private fun loadStudentDetail(): ArrayList<StudentModel>? {
        val model1 = StudentModel("Hemsworth")
        val model2 = StudentModel("Roddick")
        val model3 = StudentModel("Chrish")
        val model4 = StudentModel("Steve")
        val model5 = StudentModel("Chrish")
        val model6 = StudentModel("Nick")
        val model7 = StudentModel("Chrish")
        val model8 = StudentModel("Chrish")
        val model9 = StudentModel("Robert")
        val model10 = StudentModel("Hemsworth")
        val model11 = StudentModel("Roger")
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
    fun onBack(){
        if(fragmentNavigationIndex.value!! > 1){
            fragmentNavigationIndex.value=fragmentNavigationIndex.value!!-1
        }
    }

    private fun loadMealData(): ArrayList<MealModel>? {
        val model1 = MealModel(1, "AM Snacks","Coffee and Biscuits")
        val model2 = MealModel(2, "Lunch","Sandwich and Fruits,Milk and Banana")
        mealList.add(model1)
        mealList.add(model2)
        return mealList
    }

}