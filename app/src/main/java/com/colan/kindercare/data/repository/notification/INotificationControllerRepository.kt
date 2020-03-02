package com.colan.kindercare.data.repository.notification

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.notification.NotificationResponse

interface INotificationControllerRepository {

    suspend fun getNotificationList(schoolId: String,response: MutableLiveData<Resource<BaseResponse<List<NotificationResponse>>>>)
}