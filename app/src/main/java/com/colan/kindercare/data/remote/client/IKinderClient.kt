package com.colan.kindercare.data.remote.client

import com.colan.kindercare.data.repository.attendance.IAttendanceRepository
import com.colan.kindercare.data.repository.leaveApproval.ILeaveApprovalRepository
import com.colan.kindercare.data.repository.message.IMessageRepository
import com.colan.kindercare.data.repository.pickupPersonal.IPickupUpPersonRepository
import com.colan.kindercare.data.repository.enrolment_enquiry.IEnrolmentRepository
import com.colan.kindercare.data.repository.notification.INotificationRepository
import com.colan.kindercare.data.repository.leaveApplication.ILeaveApplicationRepository
import com.colan.kindercare.data.repository.report.IReportControllerRepository
import com.colan.kindercare.data.repository.report.IReportRepository
import com.colan.kindercare.data.repository.school.ISchoolRespository
import com.colan.kindercare.data.repository.user.IUserRepository

interface IKinderClient {
    fun getUserControllerRepository(): IUserRepository
    fun getSchoolControllerRepository(): ISchoolRespository
    fun getAttendanceControllerRepository(): IAttendanceRepository
    fun getEnrolmentControllerRepository(): IEnrolmentRepository
    fun getLeaveApprovalControllerRepository(): ILeaveApprovalRepository
    fun getLeaveApplicationControllerRepository(): ILeaveApplicationRepository
    fun getPickUpPersonControllerRepository(): IPickupUpPersonRepository
    fun getReportControllerRepository(): IReportRepository
    fun getMessageControllerRepository(): IMessageRepository
    fun getNotificationControllerRepository(): INotificationRepository
}