package com.colan.kindercare.data.repository.report

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.report.ReportAttenanceResponse
import com.colan.kindercare.data.remote.data.response.report.ReportSalaryResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IReportRepository {

    @Multipart
    @POST("api/superadmin/report/salaryfilter")
    fun ReportSalaryApi(
        @Part("fromdate") date: RequestBody,
        @Part("todate")section_id: RequestBody,
        @Part("usertype_id") usertype_id: RequestBody,
        @Part("school_id") school_id: RequestBody
    ): Call<BaseResponse<ArrayList<ReportSalaryResponse>>>

    @Multipart
    @POST("api/superadmin/workingdays/count")
    fun ReportAttendanceApi(
        @Part("fromdate") date: RequestBody,
        @Part("todate")section_id: RequestBody,
        @Part("usertype_id") usertype_id: RequestBody,
        @Part("school_id") school_id: RequestBody,
        @Part("class") classId:RequestBody,
        @Part("section")section:RequestBody
    ): Call<BaseResponse<ArrayList<ReportAttenanceResponse>>>

}