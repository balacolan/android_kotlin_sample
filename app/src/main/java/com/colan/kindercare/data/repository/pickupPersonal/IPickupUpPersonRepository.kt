package com.colan.kindercare.data.repository.pickupPersonal

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.data.remote.data.response.pickupPerson.PickupPersonListResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IPickupUpPersonRepository {

    @GET("api/superadmin/pp-approval/list/{school_id}")
    fun getPickupPersonList(@Path ("school_id") school_id:String): Call<BaseResponse<List<PickupPersonListResponse>>>

    @Multipart
    @POST("api/superadmin/pp-change-status/{id}")
    fun statusChangePickupRequest(
        @Path("id") id: String,
        @Part("_method") method: RequestBody,
        @Part("status") status: RequestBody
    ): Call<BaseResponse<Nothing>>

}