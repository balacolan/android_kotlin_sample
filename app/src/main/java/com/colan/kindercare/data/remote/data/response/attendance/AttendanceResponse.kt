package com.colan.kindercare.data.remote.data.response.attendance

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AttendanceResponse {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("profile")
    @Expose
    var profile: String? = null
    @SerializedName("sign_in_time")
    @Expose
    var signInTime: String? = null
    @SerializedName("sign_out_time")
    @Expose
    var signOutTime: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("class_id")
    @Expose
    var class_id: String? = null
    @SerializedName("section_id")
    @Expose
    var section_id: String? = null

    var mEDIT_STAFF_ATTENDANCE:Boolean = true

    var isSelected: Boolean = false
}