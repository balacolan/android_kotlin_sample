package com.colan.kindercare.data.repository.attendance

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceSectionResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class AttendanceControllerRepository(private val iAttendanceRepository: IAttendanceRepository) :
    KoinComponent,
    IAttendanceControllerRepository, BaseRespository() {
    override fun getAttendanceSection(
        schoolId: String,
        classId: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceSectionResponse>>>>
    ) {
        iAttendanceRepository.getSectionListAttendance(
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(classId)
        ).enqueue(getCallback(response))
    }

    override fun getAttendanceClass(response: MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceClassResponse>>>>) {
        iAttendanceRepository.getClassList().enqueue(getCallback(response))
    }

    override fun AddStudentAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        classId: String,
        sectionId: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iAttendanceRepository.AddStdentAttendance(
            createPlainTextRequestBody(date),
            createPlainTextRequestBody(userType),
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(log_type),
            createPlainTextRequestBody(classId),
            createPlainTextRequestBody(sectionId),
            userId,
            createPlainTextRequestBody(time)
        ).enqueue(getCallback(response))
    }

    override fun AddAdminAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iAttendanceRepository.AddAdminAttendance(
            createPlainTextRequestBody(date),
            createPlainTextRequestBody(userType),
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(log_type),
            userId,
            createPlainTextRequestBody(time)
        ).enqueue(getCallback(response))
    }

    override fun AddTeacherAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iAttendanceRepository.AddTeacherAttendance(
            createPlainTextRequestBody(date),
            createPlainTextRequestBody(userType),
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(log_type),
            userId,
            createPlainTextRequestBody(time)
        ).enqueue(getCallback(response))
    }

    override fun getStudentAttendanceList(
        //date: String,
        userType: String,
        schoolId: String,
        //log_type: String,
        //userId: ArrayList<String>,
        //time: String,
        sectionId: String,
        classId: String,
        response: MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>
    ) {
        iAttendanceRepository.getStudentAttanceList(
            createPlainTextRequestBody(userType),
            createPlainTextRequestBody(schoolId),
            createPlainTextRequestBody(classId),
            createPlainTextRequestBody(sectionId)
        ).enqueue(getCallback(response))
    }

    override fun getAttendanceList(
        date: String,
        userType: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>
    ) {
        iAttendanceRepository.getAttanceList(
            createPlainTextRequestBody(date),
            createPlainTextRequestBody(userType),
            createPlainTextRequestBody(schoolId)
        ).enqueue(getCallback(response))
    }
}