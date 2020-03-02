package com.colan.kindercare.data.model

data class AttendanceReportModel(
    var id: Int,
    var StudentName: String,
    var presentCount:String,
    var absentCount:String,
    var isAlternate:Boolean
)