package com.colan.kindercare.data.repository.school

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.SchoolListResponse
import com.colan.kindercare.data.repository.BaseRespository
import org.koin.core.KoinComponent

class SchoolControllerRepository (private val iSchoolRespository: ISchoolRespository): KoinComponent,
    ISchoolControllerRespository, BaseRespository(){

    override suspend fun getAllSchoolList(response: MutableLiveData<Resource<BaseResponse<List<SchoolListResponse>>>>) {
       iSchoolRespository.getAllSchoolList().enqueue(getCallback(response))
    }

}