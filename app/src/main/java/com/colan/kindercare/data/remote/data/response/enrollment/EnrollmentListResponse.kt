package com.colan.kindercare.data.remote.data.response.enrollment

import com.colan.kindercare.utils.dateCustom
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName



class EnrollClassResponse {
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

data class EnrollmentListResponse(
    @SerializedName("list")
    @Expose
    var data: List<EnrollDetail>? = null,
    @SerializedName("total_count")
    @Expose
    var totalCount: Int? = null,
    @SerializedName("accepted_count")
    @Expose
    var acceptedCount: Int? = null,
    @SerializedName("rejected_count")
    @Expose
    var rejectedCount: Int? = null
)

 data class EnrollDetail (
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("institute_id")
    @Expose
    var instituteId: Int? = null,
    @SerializedName("school_id")
    @Expose
    var schoolId: Int? = null,
    @SerializedName("student_name")
    @Expose
    var studentName: String? = null,
    @SerializedName("age")
    @Expose
    var age: Int? = null,
    @SerializedName("dob")
    @Expose
    var dob: String? = null,
    @SerializedName("class")
    @Expose
    var _class: String? = null,
    @SerializedName("father_name")
    @Expose
    var fatherName: String? = null,
    @SerializedName("mother_name")
    @Expose
    var motherName: String? = null,
    @SerializedName("contact")
    @Expose
    var contact: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("purpose")
    @Expose
    var purpose: String? = null,
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,
    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null

)

 {
     fun getStatusString() : String {
         var statusDetail: String? = ""
         status.let {
             when (status) {
                 0 -> {
                     statusDetail = "Pending"
                 }
                 1 -> {
                     statusDetail = "Accepted"
                 }
                 2 -> {
                     statusDetail = "Rejected"
                 }
             }
             return statusDetail!!
         }
     }

 }