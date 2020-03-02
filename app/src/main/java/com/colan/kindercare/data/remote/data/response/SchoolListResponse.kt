package com.colan.kindercare.data.remote.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class SchoolListResponse {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("institute_id")
        @Expose
        var instituteId: Int? = null
        @SerializedName("school_name")
        @Expose
        var schoolName: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null
        @SerializedName("contact")
        @Expose
        var contact: String? = null
        @SerializedName("location")
        @Expose
        var location: String? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("logo_image")
        @Expose
        var logoImage: String? = null
        @SerializedName("status")
        @Expose
        var status: Int? = null
        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        var isSelected:Boolean=false


}