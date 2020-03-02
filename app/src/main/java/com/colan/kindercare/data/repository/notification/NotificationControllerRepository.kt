package com.colan.kindercare.data.repository.notification

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.notification.NotificationResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class NotificationControllerRepository (private val iNotificationRepository: INotificationRepository):BaseRespository(),KoinComponent,INotificationControllerRepository {

    override suspend fun getNotificationList(
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<NotificationResponse>>>>
    ) {
        iNotificationRepository.getNotificationList(createPlainTextRequestBody(schoolId)).enqueue(getCallback(response))
    }
}