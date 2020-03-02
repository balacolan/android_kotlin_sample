package com.colan.kindercare.data.remote.data.response.leaveApplication

import androidx.databinding.ObservableField
import com.colan.kindercare.utils.dateFromToStringCustom
import com.colan.kindercare.utils.dateToFromDateStringCustom
import com.colan.kindercare.utils.dateYearDateStringCustom
import com.colan.kindercare.utils.getStatusTypeByName
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LeaveApplicationListResponse {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("leave_days")
    @Expose
    var leaveDays: String? = null
    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null
    @SerializedName("to_date")
    @Expose
    var toDate: String? = null
    @SerializedName("leave_type")
    @Expose
    var leaveType: String? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("contact")
    @Expose
    var contact: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("requested date")
    @Expose
    var requestedDate: String? = null

    fun getCreateDate():String {
        return fromDate?.let { dateToFromDateStringCustom(it) }!!
    }

    fun getCreateToDate():String {
        return toDate?.let { dateToFromDateStringCustom(it) }!!
    }

    fun getCreateFromYear():String {
        return fromDate?.let { dateYearDateStringCustom(it) }!!
    }

    fun getCreateToYear():String {
        return toDate?.let { dateYearDateStringCustom(it) }!!
    }

    fun getCreateDateFrom():String {
        return fromDate?.let { dateFromToStringCustom(it) }!!
    }

    fun getCreateDateTo():String {
        return toDate?.let { dateFromToStringCustom(it) }!!
    }

    fun getStatusType():String{
        return status?.let { getStatusTypeByName(it) }!!
    }

    var rapStatus  = ObservableField("")
}