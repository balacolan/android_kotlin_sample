package com.colan.kindercare.data.repository.enrolment_enquiry

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollClassResponse
import com.colan.kindercare.data.remote.data.response.enrollment.EnrollmentListResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class EnrolmentControllerRepository(private val  enrolmentControllerRepository: IEnrolmentRepository) :
    IEnrolmentControllerRepository, KoinComponent,
     BaseRespository() {
    override fun getDeleteEnroll(
        id: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        enrolmentControllerRepository.deleteEnroll(id).enqueue(getCallback(response))
    }

    override fun getEnrollClass(response: MutableLiveData<Resource<BaseResponse<ArrayList<EnrollClassResponse>>>>) {
        enrolmentControllerRepository.getClassList().enqueue(getCallback(response))
    }

    override fun getEnrollmentList(
        school_id: String,
        from_date: String?,
        to_date: String?,
        response: MutableLiveData<Resource<BaseResponse<EnrollmentListResponse>>>
    ) {
        enrolmentControllerRepository.getEnrollMentList(
            createPlainTextRequestBody(school_id),
            createPlainTextRequestBody(from_date),
            createPlainTextRequestBody(to_date)).enqueue(getCallback(response))
    }


    override fun addEnrollmentList(
        institute_id: String,
        school_id: String,
        student_name: String,
        age: String,
        dob: String,
        classname: String,
        father_name: String,
        mother_name: String,
        contact: String,
        email: String,
        purpose: String,
        status: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        enrolmentControllerRepository.addEnrollment(
            createPlainTextRequestBody(institute_id),
            createPlainTextRequestBody(school_id),
            createPlainTextRequestBody(student_name),
            createPlainTextRequestBody(age),
            createPlainTextRequestBody(dob),
            createPlainTextRequestBody(classname),
            createPlainTextRequestBody(father_name),
            createPlainTextRequestBody(mother_name),
            createPlainTextRequestBody(contact),
            createPlainTextRequestBody(email),
            createPlainTextRequestBody(purpose),
            createPlainTextRequestBody(status)
            ).enqueue(getCallback(response))
    }


}