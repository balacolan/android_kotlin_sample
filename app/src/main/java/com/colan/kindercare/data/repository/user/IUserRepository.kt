package com.colan.kindercare.data.repository.user

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.dashboard.DashboardResponse
import com.colan.kindercare.data.remote.data.response.profile.UserProfileResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ForgotPasswordResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ResetPasswordResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IUserRepository {

    @Headers("Accept: application/json")
    @Multipart
    @POST("api/user/login")
    fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Call<LoginResponse>

    @Multipart
    @POST("api/user/forgot/password")
    fun forgotPassword(
        @Part("email") email: RequestBody
    ): Call<ForgotPasswordResponse>


    @Multipart
    @POST("api/user/update/password")
    fun resetPassword(
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirm_password: RequestBody,
        @Part("otp") email: RequestBody
    ): Call<ResetPasswordResponse>


    @GET("api/userdetail/{userId}")
    fun getUserProfile(@Path("userId") userId: String):Call<BaseResponse<UserProfileResponse>>

    @Multipart
    @POST("api/user/profile/update")
    fun updateProfile(
        @Part("_method") method: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("email") email: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("address") address: RequestBody
    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/user/profile/update")
    fun updatePassword(
        @Part("_method") method: RequestBody,
        @Part("firstname") firstname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("old_password") old_password: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirm_password: RequestBody
    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/user/profileImage/update")
    fun updateProfileImage(
        @Part ("_method") method: RequestBody,
        @Part File : MultipartBody.Part?
    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/superadmin/institute_details/{institute_id}")
    fun dashboard(
        @Path("institute_id") institute_id:String,
        @Part("school_id") schoolId: RequestBody):Call<BaseResponse<List<DashboardResponse>>>

}