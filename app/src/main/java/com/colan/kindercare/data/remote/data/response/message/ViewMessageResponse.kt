package com.colan.kindercare.data.remote.data.response.message

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ViewMessageResponse {

    @SerializedName("msg_id")
    @Expose
    var msgId: String? = null
    @SerializedName("to")
    @Expose
    var to: List<String>? = null
    @SerializedName("name")
    @Expose
    var name: List<String>? = null
    @SerializedName("subject")
    @Expose
    var subject: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("attachment")
    @Expose
    var attachment: List<String>? = null
    @SerializedName("from")
    @Expose
    var from: String? = null
    @SerializedName("from_user_type")
    @Expose
    var fromUserType: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: List<Int>? = null
    @SerializedName("send_to")
    @Expose
    var sendTo: List<String>? = null
    @SerializedName("school_id")
    @Expose
    var schoolId: Int? = null
}