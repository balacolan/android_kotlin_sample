package com.colan.kindercare.data.remote.data.response.pickupPerson

import android.util.Log
import com.colan.kindercare.utils.StringToDateConvert
import com.colan.kindercare.utils.dateToStringCustom
import com.colan.kindercare.utils.dateToTimeStringCustom
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import java.text.ParseException


class PickupPersonListResponse {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("section")
    @Expose
    var section: String? = null
    @SerializedName("class")
    @Expose
    var _class: String? = null
    @SerializedName("institute_id")
    @Expose
    var instituteId: Int? = null
    @SerializedName("school_id")
    @Expose
    var schoolId: Int? = null
    @SerializedName("student_id")
    @Expose
    var studentId: Int? = null
    @SerializedName("pickup_person_name")
    @Expose
    var pickupPersonName: String? = null
    @SerializedName("relation_type")
    @Expose
    var relationType: String? = null
    @SerializedName("contact_number")
    @Expose
    var contactNumber: String? = null
    @SerializedName("date")
    @Expose
    var dates: String? = null
    @SerializedName("profile")
    @Expose
    var profile: String? = null
    @SerializedName("approval_status")
    @Expose
    var approvalStatus: String? = null
    @SerializedName("fathername")
    @Expose
    var fathername: String? = null
    @SerializedName("mothername")
    @Expose
    var mothername: String? = null

    fun getCreateDate():String {
        return dates?.let { dateToTimeStringCustom(it) }!!
    }



    inner class Student {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("institute_id")
        @Expose
        var instituteId: Int? = null
        @SerializedName("school_id")
        @Expose
        var schoolId: Int? = null
        @SerializedName("student_name")
        @Expose
        var studentName: String? = null
        @SerializedName("roll_no")
        @Expose
        var rollNo: String? = null
        @SerializedName("dob")
        @Expose
        var dob: String? = null
        @SerializedName("gender")
        @Expose
        var gender: String? = null
        @SerializedName("father_name")
        @Expose
        var fatherName: String? = null
        @SerializedName("mother_name")
        @Expose
        var motherName: String? = null
        @SerializedName("primary_email")
        @Expose
        var primaryEmail: String? = null
        @SerializedName("secondary_email")
        @Expose
        var secondaryEmail: String? = null
        @SerializedName("father_contact")
        @Expose
        var fatherContact: String? = null
        @SerializedName("mother_contact")
        @Expose
        var motherContact: Any? = null
        @SerializedName("sibilings_id")
        @Expose
        var sibilingsId: Int? = null
        @SerializedName("address")
        @Expose
        var address: String? = null
        @SerializedName("allergies")
        @Expose
        var allergies: Int? = null
        @SerializedName("student_allergy")
        @Expose
        var studentAllergy: Any? = null
        @SerializedName("pickup_person")
        @Expose
        var pickupPerson: String? = null
        @SerializedName("relationship")
        @Expose
        var relationship: String? = null
        @SerializedName("pickup_contact")
        @Expose
        var pickupContact: String? = null
        @SerializedName("class")
        @Expose
        var _class: String? = null
        @SerializedName("section")
        @Expose
        var section: String? = null
        @SerializedName("status")
        @Expose
        var status: Int? = null
        @SerializedName("created_at")
        @Expose
        var createdAt: Any? = null
        @SerializedName("updated_at")
        @Expose
        var updatedAt: Any? = null
        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null

    }
}