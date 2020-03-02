package com.colan.kindercare.data.remote.data.response.profile

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("user_row_id")
    @Expose
    var userRowId: Int? = null,
    @SerializedName("institute_id")
    @Expose
    var instituteId: Int? = null,
    @SerializedName("school_id")
    @Expose
    var schoolId: Int? = null,

    @SerializedName("parent_id")
    @Expose
    var parentId: Int? = null,
    @SerializedName("firstname")
    @Expose
    var firstname: String? = null,
    @SerializedName("lastname")
    @Expose
    var lastname: String? = null,
    @SerializedName("email")
    @Expose
    var email: String? = null,
    @SerializedName("contact")
    @Expose
    var contact: String? = null,
    @SerializedName("date_of_birth")
    @Expose
    var dateOfBirth: String? = null,
    @SerializedName("gender")
    @Expose
    var gender: Int? = null,
    @SerializedName("address")
    @Expose
    var address: Any? = null,
    @SerializedName("profile")
    @Expose
    var profile: String? = null,
    @SerializedName("usertype_id")
    @Expose
    var usertypeId: Int? = null,
    @SerializedName("status")
    @Expose
    var status: Int? = null,
    @SerializedName("verification_token")
    @Expose
    var verificationToken: Any? = null,
    @SerializedName("verified")
    @Expose
    var verified: Int? = null,
    @SerializedName("reset_key")
    @Expose
    var resetKey: Any? = null,
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,
    @SerializedName("deleted_at")
    @Expose
    var deletedAt: Any? = null,
    @SerializedName("deleted_by")
    @Expose
    var deletedBy: Any? = null
)