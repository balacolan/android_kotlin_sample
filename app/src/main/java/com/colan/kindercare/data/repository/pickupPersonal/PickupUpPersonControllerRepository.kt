package com.colan.kindercare.data.repository.pickupPersonal

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.pickupPerson.PickupPersonListResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class PickupUpPersonControllerRepository (private val iPickupUpPersonRepository: IPickupUpPersonRepository): KoinComponent,
    IPickupUpPersonControllerRepository, BaseRespository(){

    override fun statusChangePickupRequest(
        id: String,
        method: String,
        status: String,
        response: MutableLiveData<Resource<BaseResponse<Nothing>>>
    ) {
        iPickupUpPersonRepository.statusChangePickupRequest(id,createPlainTextRequestBody(method),createPlainTextRequestBody(status)).enqueue(getCallback(response))
    }

    override suspend fun getPickupPersonList(
        schoolId: String,
        response: MutableLiveData<Resource<BaseResponse<List<PickupPersonListResponse>>>>
    ) {
        iPickupUpPersonRepository.getPickupPersonList(schoolId).enqueue(getCallback(response))
    }


}