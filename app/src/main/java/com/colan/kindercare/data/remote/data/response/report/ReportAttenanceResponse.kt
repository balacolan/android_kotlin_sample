package com.colan.kindercare.data.remote.data.response.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportAttenanceResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("user_type")
    @Expose
    var userType: String? = null
    @SerializedName("working_days")
    @Expose
    var workingDays: String? = null
    @SerializedName("present_days")
    @Expose
    var presentDays: String? = null
    @SerializedName("absent_days")
    @Expose
    var absentDays: String? = null
}