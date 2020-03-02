package com.colan.kindercare.data.repository.leaveApproval

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class LeaveApprovalControllerRepository (private val iLeaveApprovalRepository: ILeaveApprovalRepository): KoinComponent,
    ILeaveApprovaControllerlRepository, BaseRespository(){

    override fun updateLeaveRequest(
        schoolId: String,
        itemId: String,
        status: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iLeaveApprovalRepository.updateLeaveRequest(createPlainTextRequestBody(schoolId),itemId,createPlainTextRequestBody(status)).enqueue(getCallback(response))
    }

    override suspend fun filterLeaveApprovalList(
        schoolId: String,
        userType: String,
        response: MutableLiveData<Resource<BaseResponse<List<LeaveApprovalListResponse>>>>
    ) {

        iLeaveApprovalRepository.filterAllLeaveApprovalList(createPlainTextRequestBody(schoolId),createPlainTextRequestBody(userType)).enqueue(getCallback(response))
    }


    override suspend fun getAllLeaveApprovalList(
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<LeaveApprovalListResponse>>>>
    ) {
        iLeaveApprovalRepository.getAllLeaveApprovalList(schoolId).enqueue(getCallback(response))
    }
}