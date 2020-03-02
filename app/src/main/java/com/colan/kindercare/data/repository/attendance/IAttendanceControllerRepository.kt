package com.colan.kindercare.data.repository.attendance

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceSectionResponse

interface IAttendanceControllerRepository {

    fun getAttendanceClass(response:MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceClassResponse>>>>)


    fun getAttendanceSection(
        schoolId: String,
        classId: String,
        response:MutableLiveData<Resource<BaseResponse<ArrayList<AttendanceSectionResponse>>>>)

    fun getAttendanceList(
        date: String,
        userType: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>
    )

    fun getStudentAttendanceList(
        userType: String,
        schoolId: String,
        sectionId: String,
        classId: String,
        response: MutableLiveData<Resource<BaseResponse<List<AttendanceResponse>>>>
    )

    fun AddAdminAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )

    fun AddTeacherAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )

    fun AddStudentAttendance(
        date: String,
        userType: String,
        schoolId: String,
        log_type: String,
        classId: String,
        sectionId: String,
        userId: ArrayList<String>,
        time: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )



    //date:String,
}