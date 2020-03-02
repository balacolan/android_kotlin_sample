package com.colan.kindercare.ui.dashboard.fragments.all_activity

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.R
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.data.model.PhotosModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel

class AllActivityViewModel (application: Application): BaseViewModel<BaseNavigator>(application){

    var phtosdModel = MutableLiveData<ArrayList<PhotosModel>>()
    private var photosList = ArrayList<PhotosModel>()

    var mealModel = MutableLiveData<ArrayList<MealModel>>()
    private var mealList = ArrayList<MealModel>()

    init {
        phtosdModel.value=loadPhotosData()
        mealModel.value=loadMealData()
    }

    private fun loadMealData(): ArrayList<MealModel>? {
        val model1 = MealModel(1, "AM Snacks","Coffee and Biscuits")
        val model2 = MealModel(2, "Lunch","Sandwich and Fruits,Milk and Banana")
        mealList.add(model1)
        mealList.add(model2)
        return mealList
    }

    private fun loadPhotosData(): ArrayList<PhotosModel>? {
        val model1 = PhotosModel(1, R.drawable.test_pic1)
        val model2 = PhotosModel(2, R.drawable.test_pic2)
        val model3 = PhotosModel(3, R.drawable.test_pic3)
        photosList.add(model1)
        photosList.add(model2)
        photosList.add(model3)
        return photosList
    }

    fun onClickAction(view:View?){
        getNavigator().onClickView(view)
    }

}