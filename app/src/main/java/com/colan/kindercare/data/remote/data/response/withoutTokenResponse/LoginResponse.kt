package com.colan.kindercare.data.remote.data.response.withoutTokenResponse

import com.colan.kindercare.data.remote.data.Errors
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse.DailyActivityByTeacher
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse.FoodCourseType





class LoginResponse {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
    @SerializedName("user_type")
    @Expose
    var userType: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("user_type_name")
    @Expose
    var userTypeName: String? = null
    @SerializedName("expires_at")
    @Expose
    var expiresAt: String? = null
    @SerializedName("institute_id")
    @Expose
    var instituteId: Int? = null
    @SerializedName("profile")
    @Expose
    var profile: String? = null

    @SerializedName("permissions")
    @Expose
    var permissions: Permissions? = null


    var error= Errors("")

    inner class Permissions {

        @SerializedName("view_salary")
        @Expose
        var viewSalary: Int? = null
        @SerializedName("add_staff")
        @Expose
        var addStaff: Int? = null
        @SerializedName("edit_staff_attendance")
        @Expose
        var editStaffAttendance: Int? = null
        @SerializedName("invoice_track_payment")
        @Expose
        var invoiceTrackPayment: Int? = null
        @SerializedName("msg_to_parents")
        @Expose
        var msgToParents: Int? = null
        @SerializedName("msg_to_teachers")
        @Expose
        var msgToTeachers: Int? = null
        @SerializedName("student_leave_approve")
        @Expose
        var studentLeaveApprove: Int? = null
        @SerializedName("multi_level_student_leave_days")
        @Expose
        var multiLevelStudentLeaveDays: Int? = null
        @SerializedName("multi_level_student_leave_approve")
        @Expose
        var multiLevelStudentLeaveApprove: Int? = null
        @SerializedName("multi_level_teacher_leave_days")
        @Expose
        var multiLevelTeacherLeaveDays: Int? = null
        @SerializedName("student_signin")
        @Expose
        var studentSignin: Int? = null
        @SerializedName("teacher_signin")
        @Expose
        var teacherSignin: Int? = null
        @SerializedName("admin_login")
        @Expose
        var adminLogin: Int? = null
        @SerializedName("food_course_type")
        @Expose
        var foodCourseType: List<FoodCourseType>? = null
        @SerializedName("daily_activity_approval")
        @Expose
        var dailyActivityApproval: Int? = null
        @SerializedName("daily_activity_by_teacher")
        @Expose
        var dailyActivityByTeacher: List<DailyActivityByTeacher>? = null
    }

    inner class FoodCourseType {

        @SerializedName("1")
        @Expose
        var _1: String? = null
        @SerializedName("2")
        @Expose
        var _2: String? = null
        @SerializedName("3")
        @Expose
        var _3: String? = null

    }

    inner class DailyActivityByTeacher {

        @SerializedName("1")
        @Expose
        var _1: String? = null
        @SerializedName("2")
        @Expose
        var _2: String? = null
        @SerializedName("4")
        @Expose
        var _4: String? = null

    }
}

