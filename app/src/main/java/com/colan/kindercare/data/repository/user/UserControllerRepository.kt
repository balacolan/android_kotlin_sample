package com.colan.kindercare.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.dashboard.DashboardResponse
import com.colan.kindercare.data.remote.data.response.profile.UserProfileResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ForgotPasswordResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ResetPasswordResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent
import java.io.File

class UserControllerRepository (private val iUserControllerRepository: IUserRepository): KoinComponent, IUserControllerRepository, BaseRespository(){

    override fun dashboard(
        instituteId: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<DashboardResponse>>>>
    ) {
        iUserControllerRepository.dashboard(instituteId,createPlainTextRequestBody(schoolId)).enqueue(getCallback(response))
    }


    override fun updatePassword(
        method: String,
        firstname: String,
        email: String,
        old_password: String,
        password: String,
        confirm_password: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iUserControllerRepository.updatePassword(createPlainTextRequestBody(method),createPlainTextRequestBody(firstname),createPlainTextRequestBody(email),createPlainTextRequestBody(old_password),createPlainTextRequestBody(password),createPlainTextRequestBody(confirm_password)).enqueue(getCallback(response))
    }

    override fun updateProfile(
        method: String,
        firstname: String,
        gender: String,
        email: String,
        dob: String,
        contact: String,
        address: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iUserControllerRepository.updateProfile(
            createPlainTextRequestBody(method),
            createPlainTextRequestBody(firstname),
            createPlainTextRequestBody(gender),
            createPlainTextRequestBody(email),
            createPlainTextRequestBody(dob),
            createPlainTextRequestBody(contact),
            createPlainTextRequestBody(address)
        ).enqueue(getCallback(response))
    }

    override fun updateProfileImage(
        method: String,
        profilePath: File?,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
       iUserControllerRepository.updateProfileImage(createPlainTextRequestBody(method),prepareFilePart("profile",profilePath)).enqueue(getCallback(response))
    }


    override fun resetPassword(
        password: String,
        confirm_password: String,
        otp: String,
        response: MutableLiveData<Resource<ResetPasswordResponse>>
    ) {
        iUserControllerRepository.resetPassword(createPlainTextRequestBody(password),createPlainTextRequestBody(confirm_password),createPlainTextRequestBody(otp)).enqueue(getCallbackForLogin(response))
    }

    override fun getUserProfile(
        userId: String,
        response: MutableLiveData<Resource<BaseResponse<UserProfileResponse>>>
    ) {
        iUserControllerRepository.getUserProfile(userId).enqueue(getCallback(response))
    }


    override fun forgotPassword(
        email: String,
        response: MutableLiveData<Resource<ForgotPasswordResponse>>
    ) {
        iUserControllerRepository.forgotPassword(createPlainTextRequestBody(email)).enqueue(getCallbackForLogin(response))
    }



    override fun login(
        email: String,
        password: String,
        response: MutableLiveData<Resource<LoginResponse>>
    ) {
        iUserControllerRepository.login(createPlainTextRequestBody(email),createPlainTextRequestBody(password)).enqueue(getCallbackForLogin(response))
    }

}