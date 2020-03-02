package com.colan.kindercare.data.repository.attendance

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceSectionResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface IAttendanceRepository {

    @Multipart
    @POST("api/attendance/list")
    fun getAttanceList(
        @Part("date") date: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("school_id") school_id: RequestBody
    ): Call<BaseResponse<List<AttendanceResponse>>>



    @Multipart
    @POST("api/attendance/student/list")
    fun getStudentAttanceList(
        //@Part("date") date: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("class_id") class_id: RequestBody,
        @Part("section_id") section_id: RequestBody
        //@Part("time") time: RequestBody,
        //@Part("log_type") log_type: RequestBody,
        //@Query("user_id[]") user_id:ArrayList<String>

    ): Call<BaseResponse<List<AttendanceResponse>>>



    @Multipart
    @POST("api/attendance/add")
    fun AddAdminAttendance(
        @Part("date") date: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("log_type") log_type: RequestBody,
        @Query("user_id[]") user_id:ArrayList<String>,
        @Part("time") time: RequestBody
    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/attendance/add")
    fun AddTeacherAttendance(
        @Part("date") date: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("log_type") log_type: RequestBody,
        @Query("user_id[]") user_id:ArrayList<String>,
        @Part("time") time: RequestBody
    ): Call<BaseResponse<Nothing>>

    @Multipart
    @POST("api/attendance/add")
    fun AddStdentAttendance(
        @Part("date") date: RequestBody,
        @Part("user_type") user_type: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("log_type") log_type: RequestBody,
        @Part("class_id")class_id: RequestBody,
        @Part("section_id")section_id: RequestBody,
        @Query("user_id[]") user_id:ArrayList<String>,
        @Part("time") time: RequestBody
    ): Call<BaseResponse<Nothing>>


    @GET("api/classname/list")
    fun getClassList():Call<BaseResponse<ArrayList<AttendanceClassResponse>>>

    @Multipart
    @POST("api/class/section/list")
    fun getSectionListAttendance(
        @Part("school_id") school_id: RequestBody,
        @Part("class_id")class_id: RequestBody
    ): Call<BaseResponse<ArrayList<AttendanceSectionResponse>>>

}