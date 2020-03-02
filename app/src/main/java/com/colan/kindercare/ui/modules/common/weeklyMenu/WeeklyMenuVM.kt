package com.colan.kindercare.ui.modules.common.weeklyMenu

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.MealModel
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeeklyMenuVM(application: Application) : BaseViewModel<BaseNavigator>(application),KoinComponent {

    var mealModel = MutableLiveData<ArrayList<MealModel>>()
    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private var mealList = ArrayList<MealModel>()
    var isParent = ObservableField(false)
    var isteacher = ObservableField(false)
    var isSuperAdmin = ObservableField(true)
    var isAdmin = ObservableField(false)

    init {
        setUpUserType()
        mealModel.value = loadMealData()
    }

    private fun loadMealData(): ArrayList<MealModel>? {
        val model1 = MealModel(1, "AM Snacks", "Coffee and Biscuits")
        val model2 = MealModel(2, "Lunch", "Sandwich and Fruits,Milk and Banana")
        mealList.add(model1)
        mealList.add(model2)
        return mealList
    }


    private fun setUpUserType() {
        when {
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "2" -> {
                isSuperAdmin = ObservableField(true)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "3" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(true)
                isteacher = ObservableField(false)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "4" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(true)
                isParent = ObservableField(false)
            }
            iSharedPreferenceService.getStringValue(Constants.USER_TYPE_ID) == "5" -> {
                isSuperAdmin = ObservableField(false)
                isAdmin = ObservableField(false)
                isteacher = ObservableField(false)
                isParent = ObservableField(true)
            }
        }
    }
}