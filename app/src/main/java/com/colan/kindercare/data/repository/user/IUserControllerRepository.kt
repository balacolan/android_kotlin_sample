package com.colan.kindercare.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.dashboard.DashboardResponse
import com.colan.kindercare.data.remote.data.response.profile.UserProfileResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ForgotPasswordResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ResetPasswordResponse
import java.io.File

interface IUserControllerRepository {
    fun login(email:String,password:String,response: MutableLiveData<Resource<LoginResponse>>)
    fun forgotPassword(email:String,response: MutableLiveData<Resource<ForgotPasswordResponse>>)
    fun resetPassword(password:String,confirm_password:String,otp:String,response: MutableLiveData<Resource<ResetPasswordResponse>>)
    fun getUserProfile(userId:String,response:MutableLiveData<Resource<BaseResponse<UserProfileResponse>>>)
    fun updatePassword(method:String,firstname: String,email: String,old_password:String,password:String,confirm_password:String,response:MutableLiveData<Resource<BaseResponse<Nothing>>>)
    fun updateProfile(method:String,firstname:String,gender:String,email:String,dob:String,contact:String,address:String,response:MutableLiveData<Resource<BaseResponse<Nothing>>>)
    fun updateProfileImage(method:String,profilePath: File?, response:MutableLiveData<Resource<BaseResponse<Nothing>>>)
    fun dashboard(instituteId:String,schoolId:String,response: MutableLiveData<Resource<BaseResponse<List<DashboardResponse>>>>)
}