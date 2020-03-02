package com.colan.kindercare.data.repository.message

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.message.UserListResponse
import com.colan.kindercare.data.remote.data.response.message.ViewMessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

interface IMessageRepository {

    @Multipart
    @POST("api/create/message")
    fun composeMessage(
        @Query("send_to[]") send_to:ArrayList<String>,
        @Part file:List<MultipartBody.Part?>,
        @Part("subject") subject: RequestBody,
        @Part("message") message: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Query("user_id[]") user_id:ArrayList<String>,
        @Part("save_type") save_type: RequestBody,
        @Part("msg_id") msg_id: RequestBody

    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/message/userlist")
    fun searchUsers(
        @Part("school_id") school_id: RequestBody,
        @Part("search_user") search_user: RequestBody,
        @Query("send_to[]") send_to:ArrayList<String>
    ): Call<BaseResponse<List<UserListResponse>>>


    @Multipart
    @POST("api/message/list")
    fun messageList(
        @Part("school_id") school_id: RequestBody,
        @Part("msg_type") msg_type: RequestBody,
        @Part("list_by") list_by: RequestBody
    ): Call<BaseResponse<List<MessageListResponse>>>


    @Multipart
    @POST("api/message/view")
    fun messageView(
        @Part("school_id") school_id: RequestBody,
        @Part("msg_id") msg_type: RequestBody
    ): Call<BaseResponse<List<ViewMessageResponse>>>

    @Multipart
    @POST("/api/message/delete")
    fun deleteMessage(
        @Part("msg_id") msg_id: RequestBody,
        @Part("msg_type") msg_type: RequestBody
    ): Call<BaseResponse<Nothing>>

}