package com.colan.kindercare.data.repository.report

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.report.ReportAttenanceResponse
import com.colan.kindercare.data.remote.data.response.report.ReportSalaryResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class ReportControllerRepository(private val iReportRepository: IReportRepository) :
    KoinComponent,
    IReportControllerRepository, BaseRespository() {
    override fun getReportAttendance(
        fromdate: String,
        todate: String,
        userType: String,
        schoolId: String,
        classId: String,
        section: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<ReportAttenanceResponse>>>>
    ) {
        iReportRepository.ReportAttendanceApi(
        createPlainTextRequestBody(fromdate),
        createPlainTextRequestBody(todate),
        createPlainTextRequestBody(userType),
        createPlainTextRequestBody(schoolId),
        createPlainTextRequestBody(classId),
        createPlainTextRequestBody(section)
        ).enqueue(getCallback(response))
    }


    override fun getReportSalary(
        fromdate: String,
        todate: String,
        usertype_id: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<ReportSalaryResponse>>>>
    ) {
        iReportRepository.ReportSalaryApi(
            createPlainTextRequestBody(fromdate),
            createPlainTextRequestBody(todate),
            createPlainTextRequestBody(usertype_id),
            createPlainTextRequestBody(schoolId)
        ).enqueue(getCallback(response))
    }

}