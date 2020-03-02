package com.colan.kindercare.data.remote.data

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("errors")
    val errors: String?,
    @SerializedName("data")
    val data: T?
)