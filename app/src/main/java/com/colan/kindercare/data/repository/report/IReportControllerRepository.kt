package com.colan.kindercare.data.repository.report

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceSectionResponse
import com.colan.kindercare.data.remote.data.response.report.ReportAttenanceResponse
import com.colan.kindercare.data.remote.data.response.report.ReportSalaryResponse

interface IReportControllerRepository {

    fun getReportAttendance(
        fromdate: String,
        todate:String,
        userType: String,
        schoolId: String,
        classId:String,
        section:String,

        response:MutableLiveData<Resource<BaseResponse<ArrayList<ReportAttenanceResponse>>>>)

    fun getReportSalary(
        fromdate: String,
        todate:String,
        usertype_id: String,
        schoolId: String,
        response:MutableLiveData<Resource<BaseResponse<ArrayList<ReportSalaryResponse>>>>)
}