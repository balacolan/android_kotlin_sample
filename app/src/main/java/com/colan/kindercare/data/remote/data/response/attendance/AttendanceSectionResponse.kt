package com.colan.kindercare.data.remote.data.response.attendance

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class AttendanceSectionResponse {
    @SerializedName("section_id")
    @Expose
    var sectionId: Int? = null
    @SerializedName("institute_id")
    @Expose
    var instituteId: Int? = null
    @SerializedName("class_id")
    @Expose
    var classId: String? = null
    @SerializedName("section")
    @Expose
    var section: String? = null
    @SerializedName("school_name")
    @Expose
    var schoolName: String? = null
    @SerializedName("school_id")
    @Expose
    var schoolId: String? = null
}