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
import org.koin.core.KoinComponent

class KinderClient (private val iKinderClientBuilder: IKinderClientBuilder) : KoinComponent, IKinderClient {
    override fun getLeaveApplicationControllerRepository(): ILeaveApplicationRepository {
        return iKinderClientBuilder.getRetrofit().create(ILeaveApplicationRepository::class.java)
    }


    override fun getNotificationControllerRepository(): INotificationRepository {
        return iKinderClientBuilder.getRetrofit().create(INotificationRepository::class.java)
    }

    override fun getReportControllerRepository(): IReportRepository {
        return iKinderClientBuilder.getRetrofit().create(IReportRepository::class.java)
    }

    override fun getMessageControllerRepository(): IMessageRepository {
        return iKinderClientBuilder.getRetrofit().create(IMessageRepository::class.java)
    }

    override fun getPickUpPersonControllerRepository(): IPickupUpPersonRepository {
        return iKinderClientBuilder.getRetrofit().create(IPickupUpPersonRepository::class.java)
    }

    override fun getLeaveApprovalControllerRepository(): ILeaveApprovalRepository {
        return iKinderClientBuilder.getRetrofit().create(ILeaveApprovalRepository::class.java)
    }

    override fun getAttendanceControllerRepository(): IAttendanceRepository {
        return iKinderClientBuilder.getRetrofit().create(IAttendanceRepository::class.java)
    }

    override fun getEnrolmentControllerRepository(): IEnrolmentRepository {
        return iKinderClientBuilder.getRetrofit().create(IEnrolmentRepository::class.java)
    }

    override fun getSchoolControllerRepository(): ISchoolRespository {
        return iKinderClientBuilder.getRetrofit().create(ISchoolRespository::class.java)
    }

    override fun getUserControllerRepository(): IUserRepository {
        return iKinderClientBuilder.getRetrofit().create(IUserRepository::class.java)
    }

}