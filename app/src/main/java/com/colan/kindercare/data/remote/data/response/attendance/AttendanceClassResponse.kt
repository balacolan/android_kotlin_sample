package com.colan.kindercare.data.remote.data.response.attendance

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class AttendanceClassResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("class_name")
    @Expose
    var className: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}