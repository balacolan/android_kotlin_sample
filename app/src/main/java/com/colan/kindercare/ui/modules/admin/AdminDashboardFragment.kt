package com.colan.kindercare.ui.modules.admin


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentAdminDashboardBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.utils.Singleton
import kotlinx.android.synthetic.main.teacher_dashboard_list_items.view.*
import kotlinx.android.synthetic.main.test_dashboard_layout.view.*
import kotlinx.android.synthetic.main.test_dashboard_layout2.view.*

/**
 * A simple [Fragment] subclass.
 */
class AdminDashboardFragment : BaseFragment<FragmentAdminDashboardBinding,AdminDashboardVM>(),BaseNavigator{

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.adminDashboardVM
    override val layoutId: Int
        get() = R.layout.fragment_admin_dashboard
    override val viewModel: AdminDashboardVM
        get() =  ViewModelProviders.of(this).get(AdminDashboardVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)

        mToobarManagner.changeToolbarTitle(getString(R.string.dashboard))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.overviewLayout?.class_layout?.header_title_tv?.text=getString(R.string.no_of_class)
        viewDataBinding?.overviewLayout?.student_layout?.header_title_tv?.text=getString(R.string.no_of_student)
        viewDataBinding?.overviewLayout?.teacher_layout?.header_title_tv?.text=getString(R.string.no_of_teacher)
        viewDataBinding?.overviewLayout?.staff_layout?.header_title_tv?.text=getString(R.string.no_of_support_staffs)

        viewDataBinding?.overviewLayout?.class_layout?.wheelprogress?.setProgressColor(R.color.purpleColor)
        viewDataBinding?.overviewLayout?.student_layout?.wheelprogress?.setProgressColor(R.color.green)
        viewDataBinding?.overviewLayout?.teacher_layout?.wheelprogress?.setProgressColor(R.color.blue)
        viewDataBinding?.overviewLayout?.staff_layout?.wheelprogress?.setProgressColor(R.color.colorOrange)

        /*student_present_layout.header_title_tv?.text=getString(R.string.no_of_students_present)
        student_absent_layout.header_title_tv?.text=getString(R.string.no_of_students_absent)
        teacher_present_layout.header_title_tv?.text=getString(R.string.no_of_teacher_present)
        teacher_absent_layout.header_title_tv?.text=getString(R.string.no_of_students_absent)*/

        viewDataBinding?.attendacneLayout?.student_present_layout?.header_title_tv?.text = getString(R.string.no_of_student_present)
        viewDataBinding?.attendacneLayout?.student_absent_layout?.header_title_tv?.text = getString(R.string.no_of_student_absent)
        viewDataBinding?.attendacneLayout?.teacher_present_layout?.header_title_tv?.text = getString(R.string.no_of_teache_present)
        viewDataBinding?.attendacneLayout?.teacher_absent_layout?.header_title_tv?.text = getString(R.string.no_of_teache_absent)

    }



    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }

}
