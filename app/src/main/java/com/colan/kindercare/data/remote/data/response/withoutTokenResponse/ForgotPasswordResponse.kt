package com.colan.kindercare.data.remote.data.response.withoutTokenResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ForgotPasswordResponse {

    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("otp")
    @Expose
    var otp: String? = null

}