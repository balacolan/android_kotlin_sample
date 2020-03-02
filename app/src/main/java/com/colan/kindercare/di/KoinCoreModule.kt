package com.colan.kindercare.di

import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.local.sharedPreference.SharedPreferenceImp
import com.colan.kindercare.data.remote.client.*
import com.colan.kindercare.data.repository.attendance.AttendanceControllerRepository
import com.colan.kindercare.data.repository.attendance.IAttendanceControllerRepository
import com.colan.kindercare.data.repository.leaveApproval.ILeaveApprovaControllerlRepository
import com.colan.kindercare.data.repository.leaveApproval.LeaveApprovalControllerRepository
import com.colan.kindercare.data.repository.message.IMessageControllerRepository
import com.colan.kindercare.data.repository.message.MessageControllerRepository
import com.colan.kindercare.data.repository.pickupPersonal.IPickupUpPersonControllerRepository
import com.colan.kindercare.data.repository.pickupPersonal.PickupUpPersonControllerRepository
import com.colan.kindercare.data.repository.enrolment_enquiry.EnrolmentControllerRepository
import com.colan.kindercare.data.repository.enrolment_enquiry.IEnrolmentControllerRepository
import com.colan.kindercare.data.repository.leaveApplication.ILeaveApplicationControllerRepository
import com.colan.kindercare.data.repository.leaveApplication.LeaveApplicationControllerRepository
import com.colan.kindercare.data.repository.notification.INotificationControllerRepository
import com.colan.kindercare.data.repository.notification.NotificationControllerRepository
import com.colan.kindercare.data.repository.report.IReportControllerRepository
import com.colan.kindercare.data.repository.report.ReportControllerRepository
import com.colan.kindercare.data.repository.school.ISchoolControllerRespository
import com.colan.kindercare.data.repository.school.SchoolControllerRepository
import com.colan.kindercare.data.repository.user.IUserControllerRepository
import com.colan.kindercare.data.repository.user.UserControllerRepository
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

class KoinCoreModule {
    val koinCoreModule = module {
        single<ISharedPreferenceService>( definition = { SharedPreferenceImp(androidApplication()) })
        single<Interceptor> { AuthTokenInterceptor(get()) }
        single<IKinderClientBuilder> { KinderClientBuilder(get(),get()) }
        single<IKinderClient> { KinderClient(get()) }
        single<IUserControllerRepository> { UserControllerRepository(get<IKinderClient>().getUserControllerRepository()) }
        single<ISchoolControllerRespository> { SchoolControllerRepository(get<IKinderClient>().getSchoolControllerRepository()) }
        single<IAttendanceControllerRepository> { AttendanceControllerRepository(get<IKinderClient>().getAttendanceControllerRepository()) }
        single<ILeaveApprovaControllerlRepository> { LeaveApprovalControllerRepository(get<IKinderClient>().getLeaveApprovalControllerRepository()) }
        single<ILeaveApplicationControllerRepository> { LeaveApplicationControllerRepository(get<IKinderClient>().getLeaveApplicationControllerRepository()) }
        single<IPickupUpPersonControllerRepository> { PickupUpPersonControllerRepository(get<IKinderClient>().getPickUpPersonControllerRepository()) }
        single<IEnrolmentControllerRepository> { EnrolmentControllerRepository(get<IKinderClient>().getEnrolmentControllerRepository()) }
        single<IMessageControllerRepository> { MessageControllerRepository(get<IKinderClient>().getMessageControllerRepository()) }
        single<IReportControllerRepository> { ReportControllerRepository(get<IKinderClient>().getReportControllerRepository()) }
        single<INotificationControllerRepository> { NotificationControllerRepository(get<IKinderClient>().getNotificationControllerRepository()) }
    }
}