package com.colan.kindercare.data.repository.leaveApplication

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.leaveApplication.LeaveApplicationListResponse

interface ILeaveApplicationControllerRepository {

    fun addLeaveApplication(
        schoolId: String,
        //leaveDays: String,
        fromdate: String,
        todate: String,
        leaveType: String,
        reason: String,
        contactNo: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )

    fun getLeaveFilterData(
        fromdate: String,
        todate: String,
        leave_type: String,
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>
    )

    fun getLeaveApplicaionList(response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>)

    fun getLeaveItemIdData(
        id: String,
        response: MutableLiveData<Resource<BaseResponse<ArrayList<LeaveApplicationListResponse>>>>
    )

    fun getLeaveItemIdDelete(
        id: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    )
}