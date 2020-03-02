package com.colan.kindercare.data.remote.data.response.message

import com.colan.kindercare.utils.dateToTimeStringCustom2
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MessageListResponse {

    @SerializedName("msg_id")
    @Expose
    var msgId: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("profile")
    @Expose
    var profile: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("subject")
    @Expose
    var subject: String? = null
    @SerializedName("from_id")
    @Expose
    var fromId: Int? = null
    @SerializedName("to_type")
    @Expose
    var toType: String? = null

    @SerializedName("read_status")
    @Expose
    var read_status: Int? = null

    @SerializedName("receiver_type")
    @Expose
    var receiver_type: String? = null
    


    fun getCreateDate():String {
        return date?.let { dateToTimeStringCustom2(it) }!!
    }
}