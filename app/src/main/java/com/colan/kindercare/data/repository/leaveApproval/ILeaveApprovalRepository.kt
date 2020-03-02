package com.colan.kindercare.data.repository.leaveApproval

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ILeaveApprovalRepository {

    @GET("api/superadmin/leaveapproval/list")
    fun getAllLeaveApprovalList(@Query ("school_id") schoolId:String): Call<BaseResponse<List<LeaveApprovalListResponse>>>

    @Multipart
    @POST("api/superadmin/leaveapproval/filter")
    fun filterAllLeaveApprovalList(
        @Part("school_id") school_id: RequestBody,
        @Part("user_type") user_type: RequestBody
    ): Call<BaseResponse<List<LeaveApprovalListResponse>>>

    @Multipart
    @POST("api/superadmin/leaveapproval/update/{item_id}")
    fun updateLeaveRequest(
        @Part("school_id") school_id: RequestBody,
        @Path("item_id") item_id: String,
        @Part("status") status: RequestBody
    ): Call<BaseResponse<Nothing>>

}