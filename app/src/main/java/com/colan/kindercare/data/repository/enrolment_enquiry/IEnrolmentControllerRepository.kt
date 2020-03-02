package com.colan.kindercare.data.repository.enrolment_enquiry

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollmentListResponse

interface IEnrolmentControllerRepository {
    fun getEnrollClass(response:MutableLiveData<Resource<BaseResponse<ArrayList<EnrollClassResponse>>>>)
    fun getDeleteEnroll(id:String,response:  MutableLiveData<Resource<BaseResponse<Nothing>>>)
    fun getEnrollmentList(school_id:String,from_date:String?,to_date:String?,response: MutableLiveData<Resource<BaseResponse<EnrollmentListResponse>>>)
    fun addEnrollmentList(institute_id:String,school_id:String,student_name:String,age:String,dob:String,classname:String,father_name:String,mother_name:String,contact:String,email:String,purpose:String,status:String,response: MutableLiveData<Resource<BaseResponse<Nothing>>>)
}