package com.colan.kindercare.data.remote.data.response.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
 class DashboardResponse {

     @SerializedName("techer")
     @Expose
     var techer: Int? = null
     @SerializedName("support staff")
     @Expose
     var supportStaff: Int? = null
     @SerializedName("student")
     @Expose
     var student: Int? = null
     @SerializedName("admins count")
     @Expose
     var adminsCount: Int? = null
     @SerializedName("branches")
     @Expose
     var branches: Int? = null
     @SerializedName("admins birthday")
     @Expose
     var adminsBirthday: List<Any>? = null
}