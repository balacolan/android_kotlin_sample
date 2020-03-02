package com.colan.kindercare.data.repository.leaveApplication

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApplication.LeaveApplicationListResponse
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class LeaveApplicationControllerRepository(private val iLeaveApplicationRepository: ILeaveApplicationRepository) :
    KoinComponent,
    ILeaveApplicationControllerRepository, BaseRespository() {

    override fun getLeaveItemIdDelete(
        id: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iLeaveApplicationRepository.getLeaveListDelete(id).enqueue(getCallback(response))
    }

    override fun getLeaveItemIdData(
        id: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>
    ) {
        iLeaveApplicationRepository.getItemIdData(id).enqueue(getCallback(response))
    }

    override fun getLeaveApplicaionList(response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>) {
        iLeaveApplicationRepository.getLeaveApplicationList().enqueue(getCallback(response))
    }

    override fun addLeaveApplication(
        schoolId: String,
        //leaveDays: String,
        fromdate: String,
        todate: String,
        leaveType: String,
        reason: String,
        contactNo: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iLeaveApplicationRepository.addLeaveApplicationList(
            createPlainTextRequestBody(schoolId),
            //createPlainTextRequestBody(leaveDays),
            createPlainTextRequestBody(fromdate),
            createPlainTextRequestBody(todate),
            createPlainTextRequestBody(leaveType),
            createPlainTextRequestBody(reason),
            createPlainTextRequestBody(contactNo)
        ).enqueue(getCallback(response))
    }

    override fun getLeaveFilterData(
        fromdate: String,
        todate: String,
        leave_type: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>
    ) {
        iLeaveApplicationRepository.filterAllLeaveApplicationList(
            createPlainTextRequestBody(fromdate),
            createPlainTextRequestBody(todate),
            createPlainTextRequestBody(leave_type),
            createPlainTextRequestBody(schoolId)
        ).enqueue(getCallback(response))
    }


}