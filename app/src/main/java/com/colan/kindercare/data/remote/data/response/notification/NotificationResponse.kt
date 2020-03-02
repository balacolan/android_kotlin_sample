package com.colan.kindercare.data.remote.data.response.notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class NotificationResponse {

    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("leave_days")
    @Expose
    var leaveDays: Int? = null


}