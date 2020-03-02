package com.colan.kindercare.data.repository.leaveApproval

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApproval.LeaveApprovalListResponse

interface ILeaveApprovaControllerlRepository {

    suspend fun getAllLeaveApprovalList(
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<LeaveApprovalListResponse>>>>
    )

    suspend fun filterLeaveApprovalList(
        schoolId: String,
        userType: String,
        response: MutableLiveData<Resource<BaseResponse<List<LeaveApprovalListResponse>>>>
    )

    fun updateLeaveRequest(
        schoolId: String,
        itemId: String,
        status: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )

}