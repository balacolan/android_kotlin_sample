package com.colan.kindercare.data.repository.message

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.message.UserListResponse
import com.colan.kindercare.data.remote.data.response.message.ViewMessageResponse
import java.io.File

interface IMessageControllerRepository {

    fun composeMessage(
        sendTo:ArrayList<String>,
        attachment: ArrayList<File?>?,
        subject:String,
        message:String,
        schoolId:String,
        userId:ArrayList<String>,
        saveType:String,
        msgId:String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>)
    suspend fun getMessageList(schoolId: String,msgType:String,listBy:String,response: MutableLiveData<Resource<BaseResponse<List<MessageListResponse>>>>)
    suspend fun getUserList(schoolId: String,search_user:String,send_to:ArrayList<String>,response: MutableLiveData<Resource<BaseResponse<List<UserListResponse>>>>)
    suspend fun viewMessage(schoolId: String,msgId:String,response: MutableLiveData<Resource<BaseResponse<List<ViewMessageResponse>>>>)
    suspend fun deleteMessage(msgId: String,msgType:String,response: MutableLiveData<Resource<BaseResponse<Nothing>>>)
}