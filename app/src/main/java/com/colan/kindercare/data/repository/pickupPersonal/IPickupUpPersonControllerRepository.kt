package com.colan.kindercare.data.repository.pickupPersonal

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.attendance.AttendanceResponse
import com.colan.kindercare.data.remote.data.response.pickupPerson.PickupPersonListResponse

interface IPickupUpPersonControllerRepository {

    suspend fun getPickupPersonList(schoolId:String,response: MutableLiveData<Resource<BaseResponse<List<PickupPersonListResponse>>>>)
    fun statusChangePickupRequest(id:String,method:String,status:String,response: MutableLiveData<Resource<BaseResponse<Nothing>>>)
}