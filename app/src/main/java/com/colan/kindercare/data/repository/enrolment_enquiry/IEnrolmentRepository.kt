package com.colan.kindercare.data.repository.enrolment_enquiry

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollmentListResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IEnrolmentRepository {
    @Multipart
    @POST("api/superadmin/get_enquiry_list")
    fun getEnrollMentList(
        @Part("school_id") school_id: RequestBody,
        @Part("from_date") from_date: RequestBody?,
        @Part("to_date") to_date: RequestBody?
    ): Call<BaseResponse<EnrollmentListResponse>>




    @GET("/api/superadmin/enquiry_delete/{school_id}")
    fun deleteEnroll(
        @Path("school_id") school_id: String): Call<BaseResponse<Nothing>>


    @GET("api/classname/list")
    fun getClassList():Call<BaseResponse<ArrayList<EnrollClassResponse>>>

    @Multipart
    @POST("/api/superadmin/enquiry_add")
    fun addEnrollment(
        @Part("institute_id") institute_id: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("student_name") student_name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("class") classname: RequestBody,
        @Part("father_name") father_name: RequestBody,
        @Part("mother_name") mother_name: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("email") email: RequestBody,
        @Part("purpose") purpose: RequestBody,
        @Part("status") status: RequestBody
    ): Call<BaseResponse<Nothing>>
}