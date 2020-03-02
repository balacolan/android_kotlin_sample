package com.colan.kindercare.ui.modules.superAdmin


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.FragmentSuperAdminDashboardBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.utils.SchoolSelectionBroadCast
import com.colan.kindercare.utils.SchoolSelectionListner
import com.colan.kindercare.utils.Singleton
import kotlinx.android.synthetic.main.teacher_dashboard_list_items.view.*
import kotlinx.android.synthetic.main.test_dashboard_layout.view.*


/**
 * A simple [Fragment] subclass.
 */
class SuperAdminDashboardFragment :
    BaseFragment<FragmentSuperAdminDashboardBinding, SuperAdminDashboardVM>(),
    BaseNavigator, SchoolSelectionListner {

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.superAdminDashboardVM
    override val layoutId: Int
        get() = R.layout.fragment_super_admin_dashboard
    override val viewModel: SuperAdminDashboardVM
        get() = ViewModelProviders.of(this).get(SuperAdminDashboardVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        SchoolSelectionBroadCast.schoolLister = this
        observeResponse()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToobarManagner.changeToolbarTitle(getString(R.string.dashboard))
        setUpCount()
        viewDataBinding?.overviewLayout?.overview_lable?.visibility = View.GONE
        val params =
            viewDataBinding?.overviewLayout?.view1?.layoutParams as ConstraintLayout.LayoutParams
        params.setMargins(0, 0, 0, 0) //substitute parameters for left, top, right, bottom
        viewDataBinding?.overviewLayout?.view1?.layoutParams = params
        viewDataBinding?.overviewLayout?.class_layout?.header_title_tv?.text =
            getString(R.string.no_of_students)
        viewDataBinding?.overviewLayout?.student_layout?.header_title_tv?.text =
            getString(R.string.no_of_admin_users)
        viewDataBinding?.overviewLayout?.teacher_layout?.header_title_tv?.text =
            getString(R.string.no_of_teacher)
        viewDataBinding?.overviewLayout?.staff_layout?.header_title_tv?.text =
            getString(R.string.no_of_support_staffs)

        viewDataBinding?.overviewLayout?.class_layout?.wheelprogress?.setDefText(
            defText = getString(
                R.string.students
            )
        )
        viewDataBinding?.overviewLayout?.student_layout?.wheelprogress?.setDefText(
            defText = getString(
                R.string.admins
            )
        )
        viewDataBinding?.overviewLayout?.teacher_layout?.wheelprogress?.setDefText(
            defText = getString(
                R.string.teachers
            )
        )
        viewDataBinding?.overviewLayout?.staff_layout?.wheelprogress?.setDefText(
            defText = getString(
                R.string.staffs
            )
        )
        viewDataBinding?.overviewLayout?.class_layout?.wheelprogress?.setProgressColor(
            getColor(
                context!!,
                R.color.bg_db_school
            )
        )
        viewDataBinding?.overviewLayout?.student_layout?.wheelprogress?.setProgressColor(
            getColor(
                context!!,
                R.color.bg_db_admin
            )
        )
        viewDataBinding?.overviewLayout?.teacher_layout?.wheelprogress?.setProgressColor(
            getColor(
                context!!,
                R.color.bg_db_teacher
            )
        )
        viewDataBinding?.overviewLayout?.staff_layout?.wheelprogress?.setProgressColor(
            getColor(
                context!!,
                R.color.bg_db_staff
            )
        )
    }

    private fun observeResponse() {
        viewModel.response.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    viewModel.noOfStaffs.set(it.data?.data!![0].supportStaff.toString())
                    viewModel.noOfAdmin.set(it.data.data[0].adminsCount.toString())
                    viewModel.noOfStutents.set(it.data.data[0].student.toString())
                    viewModel.noOfTeachers.set(it.data.data[0].techer.toString())

                    setUpCount()
                }
                Status.ERROR -> {

                }
                else -> {

                }
            }

        })
    }


    private fun setUpCount() {
        viewDataBinding?.overviewLayout?.class_layout?.wheelprogress?.setStepCountText(
            viewModel.noOfStutents.get().toString()
        )
        viewDataBinding?.overviewLayout?.student_layout?.wheelprogress?.setStepCountText(
            viewModel.noOfAdmin.get().toString()
        )
        viewDataBinding?.overviewLayout?.teacher_layout?.wheelprogress?.setStepCountText(
            viewModel.noOfTeachers.get().toString()
        )
        viewDataBinding?.overviewLayout?.staff_layout?.wheelprogress?.setStepCountText(
            viewModel.noOfStaffs.get().toString()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible = true
        mToobarManagner = context as IToolbar
    }

    override fun onTriggerSchoolSelection() {
        viewModel.loadDashBoardData()
    }

    override fun onClickView(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
