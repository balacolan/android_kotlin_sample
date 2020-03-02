package com.colan.kindercare.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.WithOutTokenBaseResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.lang.Exception

open class BaseRespository {

    fun <T> getCallback(responseData: MutableLiveData<Resource<BaseResponse<T>>>): Callback<BaseResponse<T>> {
        return object : Callback<BaseResponse<T>> {
            override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
                if (t is IOException)
                    responseData.value = Resource.failure("Server time out", null)
                else
                    responseData.value = Resource.failure(t.message!!, null)
            }
            override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
                if (response.isSuccessful && response.body() != null) {
                        responseData.value = Resource.success(response.body()!!)

                } else {
                    try {
                        if(response.code()==204){
                            responseData.value = Resource.failure("204",null)
                        }else{
                            val errorResponse = getErrorResponse<T>(response.errorBody()!!)
                            responseData.value = Resource.failure(response.code().toString(),errorResponse)
                        }
                    }catch (t:Exception){

                    }
                }
            }
        }
    }

    fun <T> getErrorResponse(responseBody: ResponseBody): BaseResponse<T> {
        var truckErrorResponse: BaseResponse<T>
        try {
            val jObjError = JSONObject(responseBody.string())
            val listPathObject = jObjError.getJSONObject("errors")
            val iter = listPathObject.keys() //This should be the iterator you want.
            val key = iter.next()
            truckErrorResponse = BaseResponse(
               errors =listPathObject.getString(key),
                data = null
            )
        } catch (e: JSONException) {
            truckErrorResponse = BaseResponse(
                errors = null,
                data = null)
        }
        return truckErrorResponse
    }


    fun <T> getCallbackForLogin(responseData: MutableLiveData<Resource<T>>): Callback<T> {
        return object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is IOException)
                    responseData.value=Resource.failure("Server time out", null)
                else
                    responseData.value=Resource.failure(t.message!!, null)
            }
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful && response.body() != null) {
                    responseData.value= Resource.success(response.body()!!)
                } else {
                    val errorResponse = getLoginErrorResponse<LoginResponse>(response.errorBody()!!)
                        responseData.value = Resource.failure(""+errorResponse.error.message, null)
                }
            }
        }
    }

    fun <T> getLoginErrorResponse(responseBody: ResponseBody): LoginResponse {
        val loginErrorResponse=
            LoginResponse()
        try {
            val jObjError = JSONObject(responseBody.string())
            val jObjError2=jObjError.getJSONObject("errors")
            loginErrorResponse.error.message  =jObjError2.getString("msg")
        } catch (e: JSONException) {

        }
        return loginErrorResponse
    }


    fun createPlainTextRequestBody(data: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), data ?: "")
    }


    fun getRequestBody(file: File?): RequestBody? {
        if (file != null) {
            return if (file.exists()) {
                RequestBody.create(MediaType.parse("image/*"), file)
            } else {
                null
            }
        }else {
            return null
        }
    }

    fun prepareFilePart(partName: String, file: File?): MultipartBody.Part? {
        val requestBody = getRequestBody(file)
        return if (requestBody != null) {
            MultipartBody.Part.createFormData(partName, file!!.name, requestBody)
        } else {
            null
        }
    }

    fun prepareListOfFilePart(partName: String, file: ArrayList<File?>?): List<MultipartBody.Part?> {
        val filePart=ArrayList<MultipartBody.Part?>()
        file?.filter {
            val requestBody = getRequestBody(it)
            filePart.addAll( listOf(
                if (requestBody != null) {
                    MultipartBody.Part.createFormData(partName, it!!.name, requestBody)
                } else {
                    null
                }
            ))
        }

        return filePart
    }

}