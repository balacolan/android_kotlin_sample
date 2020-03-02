package com.colan.kindercare.data.remote.data.response.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportSalaryResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("user_type")
    @Expose
    var userType: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("salary")
    @Expose
    var salary: String? = null
}