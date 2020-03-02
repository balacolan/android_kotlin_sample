package com.colan.kindercare.ui.modules.parent.familyinformation

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel

class FamilyInformationVM(application: Application) : BaseViewModel<BaseNavigator>(application) {

    val hasSpinnerFocused=ObservableField(false)
    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }
}