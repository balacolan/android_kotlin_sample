package com.colan.kindercare.data.repository.notification

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.notification.NotificationResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface INotificationRepository {

    @Multipart
    @POST("api/superadmin/notification")
    fun getNotificationList(
        @Part("school_id") school_id: RequestBody
    ): Call<BaseResponse<List<NotificationResponse>>>
}