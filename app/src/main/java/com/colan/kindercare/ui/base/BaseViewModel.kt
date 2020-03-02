package com.colan.kindercare.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.colan.kindercare.utils.NetworkUtils
import com.colan.kindercare.utils.toast


abstract class BaseViewModel<N>(application: Application) : AndroidViewModel(application) {

    private val mApplication: Application = application
    private var mNavigator: N? = null

    fun isNetworkConnected(): Boolean {
        val network = NetworkUtils.isNetworkConnected(mApplication)
        if (!network) putToast("No Network, Please check internet connection.")
        return network
    }

     fun putToast(msg: String) {
        mApplication.toast(msg)
    }

    fun getNavigator(): N {
        return mNavigator!!
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = navigator
    }
}