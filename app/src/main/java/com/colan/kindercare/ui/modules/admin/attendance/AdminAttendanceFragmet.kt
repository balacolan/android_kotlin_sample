package com.colan.kindercare.ui.modules.admin.attendance


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentAdminAttendanceFragmetBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.admin.attendance.admin.AttendanceAdminFragment
import com.colan.kindercare.ui.modules.admin.attendance.student.AdminStudentAttendanceFragment
import com.colan.kindercare.ui.modules.admin.attendance.teacher.AdminTeacherAttendanceFragment
import com.colan.kindercare.ui.modules.parent.payment.IRadioListener
import com.colan.kindercare.utils.*
import kotlinx.android.synthetic.main.date_layout.view.*

/**
 * A simple [Fragment] subclass.
 */
class AdminAttendanceFragmet :
    BaseFragment<FragmentAdminAttendanceFragmetBinding, AdminAttendanceVM>(),
    IRadioListener, View.OnClickListener {

    private lateinit var mToobarManagner: IToolbar
    private var type: String = ""

    override val bindingVariable: Int
        get() = BR.adminAttendantVM
    override val layoutId: Int
        get() = R.layout.fragment_admin_attendance_fragmet
    override val viewModel: AdminAttendanceVM
        get() = ViewModelProviders.of(this).get(AdminAttendanceVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        if (viewModel.isSuperAdmin.get()!!) {
            mToobarManagner.changeToolbarTitle(getString(R.string.daily_attendance_title))
        } else if (viewModel.isAdmin.get()!!) {
            mToobarManagner.changeToolbarTitle(getString(R.string.daily_attendance_title))
        } else {
            mToobarManagner.changeToolbarTitle(getString(R.string.attendance))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isSuperAdmin.get()!!) {
            viewDataBinding?.teacherLayout?.visibility = View.GONE
            viewDataBinding?.studentLayout?.visibility = View.GONE
        }
        viewDataBinding?.dateLayout?.date_next_iv?.setOnClickListener(this)
        viewDataBinding?.dateLayout?.date_previous_iv?.setOnClickListener(this)
        viewModel.currentDate.observe(this, Observer {
            viewDataBinding?.dateLayout?.tv_cal_date?.setText(viewModel.currentDate.value)
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
        replaceFragment(
            R.id.attendance_container,
            AdminTeacherAttendanceFragment(),
            "Teacher",
            "Teacher"
        )
    }


    override fun onRadioButtonClick(id: Int) {
        when (id) {
            R.id.admin_rb -> {
                viewModel.adminSelected.set(true)
                viewModel.teacherSelected.set(false)
                viewModel.studentSelected.set(false)
                type = Constants.ADMIN_TYPE
                navigateToSelectedFragment(Constants.ADMIN_TYPE)
            }
            R.id.teacher_rb -> {
                viewModel.adminSelected.set(false)
                viewModel.teacherSelected.set(true)
                viewModel.studentSelected.set(false)
                if (!viewModel.isSuperAdmin.get()!!) {
                    viewDataBinding?.teacherLayout?.visibility = View.VISIBLE
                    viewDataBinding?.studentLayout?.visibility = View.GONE
                }
                type = Constants.TEACHER_TYPE
                navigateToSelectedFragment(Constants.TEACHER_TYPE)
            }
            R.id.student_rb -> {
                viewModel.adminSelected.set(false)
                viewModel.studentSelected.set(true)
                viewModel.teacherSelected.set(false)
                if (!viewModel.isSuperAdmin.get()!!) {
                    viewDataBinding?.teacherLayout?.visibility = View.GONE
                    viewDataBinding?.studentLayout?.visibility = View.VISIBLE
                }
                type = Constants.STUDENT_TYPE
                navigateToSelectedFragment(Constants.STUDENT_TYPE)
            }
        }
    }


    private fun navigateToSelectedFragment(type: String) {
        when (type) {
            Constants.STUDENT_TYPE -> {
                replaceFragment(AdminStudentAttendanceFragment(), Constants.STUDENT_TYPE)
            }
            Constants.TEACHER_TYPE -> {
                replaceFragment(AdminTeacherAttendanceFragment(), Constants.TEACHER_TYPE)
            }
            Constants.ADMIN_TYPE -> {
                replaceFragment(AttendanceAdminFragment(), Constants.ADMIN_TYPE)
            }
        }
    }


    private fun replaceFragment(frament: Fragment, type: String) {
        replaceFragment(
            R.id.attendance_container,
            frament,
            type,
            type
        )
    }

    override fun onClickView(v: View?) {

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.date_previous_iv -> {
                val date = viewModel.currentDate.value?.let { StringToDateConvert(it) }
                val decreamentDate = date?.let { decrementDateByOne(it) }
                val dateInString = decreamentDate?.toString("dd MMMM yyyy")
                val dateInApiFormat = decreamentDate?.toString("yyyy-MM-dd")
                viewModel.currentDate.value = dateInString
                dateInApiFormat?.let {
                    viewModel.iSharedPreferenceService.setStringValue(
                        Constants.SELECTED_DATE,
                        it
                    )
                }
                navigateToSelectedFragment(type)
            }
            R.id.date_next_iv -> {
                val date = viewModel.currentDate.value?.let { StringToDateConvert(it) }
                val incrementDate = date?.let { incrementDateByOne(it) }
                val dateInString = incrementDate?.toString("dd MMMM yyyy")
                val dateInApiFormat = incrementDate?.toString("yyyy-MM-dd")
                viewModel.currentDate.value = dateInString
                dateInApiFormat?.let {
                    viewModel.iSharedPreferenceService.setStringValue(
                        Constants.SELECTED_DATE,
                        it
                    )
                }
                navigateToSelectedFragment(type)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
