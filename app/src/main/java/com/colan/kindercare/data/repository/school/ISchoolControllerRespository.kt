package com.colan.kindercare.data.repository.school

import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.SchoolListResponse

interface ISchoolControllerRespository {
    suspend fun getAllSchoolList(response: MutableLiveData<Resource<BaseResponse<List<SchoolListResponse>>>>)
}