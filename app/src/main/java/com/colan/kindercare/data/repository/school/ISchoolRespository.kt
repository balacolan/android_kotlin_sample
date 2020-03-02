package com.colan.kindercare.data.repository.school

import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.response.SchoolListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ISchoolRespository {

    @GET("api/superadmin/school/list")
    fun getAllSchoolList(): Call<BaseResponse<List<SchoolListResponse>>>
}