package com.colan.kindercare.ui.modules.common.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.model.NotificationModel
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.notification.NotificationResponse
import com.colan.kindercare.data.repository.notification.INotificationControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationViewModel(application: Application) : BaseViewModel<BaseNavigator>(application),KoinComponent {

    var notificationList = ArrayList<NotificationResponse>()
    val iSharedPreferenceService: ISharedPreferenceService by inject()
    private val iNotificationControllerRepository: INotificationControllerRepository by inject()
    var getNotificationListApiJob: Job? = null
    val notificationListResponse = MutableLiveData<Resource<BaseResponse<List<NotificationResponse>>>>()
    val mShowProgressBar = SingleLiveData<Boolean>()
    init {
        loadNotificationData()
    }

    private fun loadNotificationData(){
        GlobalScope.launch {
            getNotificationListApiJob = async(Dispatchers.IO) {
                iNotificationControllerRepository.getNotificationList(iSharedPreferenceService.getStringValue(Constants.SCHOOL_ID),notificationListResponse)
            }
        }
    }

}