package com.colan.kindercare.data.repository.message

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.message.MessageListResponse
import com.colan.kindercare.data.remote.data.response.message.UserListResponse
import com.colan.kindercare.data.remote.data.response.message.ViewMessageResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent
import java.io.File

class MessageControllerRepository(private val iMessageRepository: IMessageRepository) :
    KoinComponent,
    IMessageControllerRepository, BaseRespository() {

    override suspend fun deleteMessage(
        msgId: String,
        msgType: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
       iMessageRepository.deleteMessage(createPlainTextRequestBody(msgId),createPlainTextRequestBody(msgType)).enqueue(getCallback(response))
    }

    override suspend fun viewMessage(
        schoolId: String,
        msgId: String,
        response: MutableLiveData<Resource<BaseResponse<List<ViewMessageResponse>>>>
    ) {
        iMessageRepository.messageView(createPlainTextRequestBody(schoolId),createPlainTextRequestBody(msgId)).enqueue(getCallback(response))
    }

    override suspend fun getUserList(
        schoolId: String,
        search_user: String,
        send_to: ArrayList<String>,
        response: MutableLiveData<Resource<BaseResponse<List<UserListResponse>>>>
    ) {
        iMessageRepository.searchUsers(
            createPlainTextRequestBody(schoolId),createPlainTextRequestBody(search_user),send_to
        ).enqueue(getCallback(response))
    }

    override suspend fun getMessageList(
        schoolId: String,
        msgType: String,
        listBy: String,
        response: MutableLiveData<Resource<BaseResponse<List<MessageListResponse>>>>
    ) {
        iMessageRepository.messageList(
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(msgType),
            createPlainTextRequestBody(listBy)
        ).enqueue(getCallback(response))
    }

    override fun composeMessage(
        sendTo: ArrayList<String>,
        attachment: ArrayList<File?>?,
        subject: String,
        message: String,
        schoolId: String,
        userId: ArrayList<String>,
        saveType: String,
        msgId: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iMessageRepository.composeMessage(
            sendTo,
            prepareListOfFilePart("attachment[]",attachment),
            createPlainTextRequestBody(subject),
            createPlainTextRequestBody(message),
            createPlainTextRequestBody(schoolId),
            userId,
            createPlainTextRequestBody(saveType),
            createPlainTextRequestBody(msgId)
        ).enqueue(getCallback(response))
    }
}