package com.colan.kindercare.data.remote.data.response.leaveApproval

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class LeaveApprovalListResponse {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null
    @SerializedName("to_date")
    @Expose
    var toDate: String? = null
    @SerializedName("tot_days")
    @Expose
    var totDays: Int? = null
    @SerializedName("reason")
    @Expose
    var reason: String? = null
    @SerializedName("contact_no")
    @Expose
    var contactNo: String? = null
    @SerializedName("requested_date")
    @Expose
    var requestedDate: String? = null
    @SerializedName("profile")
    @Expose
    var profile: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
}