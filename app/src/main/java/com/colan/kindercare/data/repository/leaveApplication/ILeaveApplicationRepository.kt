package com.colan.kindercare.data.repository.leaveApplication

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.leaveApplication.LeaveApplicationListResponse
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ILeaveApplicationRepository {

    @GET("api/admin/leaverequest/view/{id}")
    fun getItemIdData(@Path("id") schoolId: String): Call<BaseResponse<ArrayList<LeaveApplicationListResponse>>>

    @Multipart
    @POST("api/admin/leaverequest/add")
    fun addLeaveApplicationList(
        @Part("school_id") school_id: RequestBody,
        //@Part("leave_days") leave_type: RequestBody,
        @Part("from_date") from_date: RequestBody,
        @Part("to_date") to_date: RequestBody,
        @Part("leave_type") leave_days: RequestBody,
        @Part("reason") reason: RequestBody,
        @Part("contact") contact: RequestBody
    ): Call<BaseResponse<Nothing>>


    @Multipart
    @POST("api/admin/leaverequest/filter")
    fun filterAllLeaveApplicationList(
        @Part("fromdate") from_date: RequestBody,
        @Part("todate") to_date: RequestBody,
        @Part("leave_type") leave_type: RequestBody,
        @Part("school_id") school_id: RequestBody
    ): Call<BaseResponse<ArrayList<LeaveApplicationListResponse>>>

    @GET("api/admin/leaverequest/list")
    fun getLeaveApplicationList(): Call<BaseResponse<ArrayList<LeaveApplicationListResponse>>>

    /*{{apiUrl}}/api/admin/leaverequest/delete/66*/

    @POST("api/admin/leaverequest/delete/{id}")
    fun getLeaveListDelete(@Path("id") leaveId: String): Call<BaseResponse<Nothing>>

}