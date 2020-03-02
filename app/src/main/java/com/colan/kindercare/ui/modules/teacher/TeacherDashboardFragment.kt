package com.colan.kindercare.ui.modules.teacher


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentTeacherDashboardBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.teacher_dashboard_list_items.view.*
import kotlinx.android.synthetic.main.test_dashboard_layout.view.*
import kotlinx.android.synthetic.main.test_dashboard_layout2.*

/**
 * A simple [Fragment] subclass.
 */
class TeacherDashboardFragment :
    BaseFragment<FragmentTeacherDashboardBinding, TeacherDashboardVM>(), BaseNavigator {

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.teacherDashboardVM
    override val layoutId: Int
        get() = R.layout.fragment_teacher_dashboard
    override val viewModel: TeacherDashboardVM
        get() = ViewModelProviders.of(this).get(TeacherDashboardVM::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.dashboard))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding?.classLayout?.headerTitleTv?.text=getString(R.string.no_of_class)
        viewDataBinding?.studentLayout?.headerTitleTv?.text=getString(R.string.no_of_student)
        viewDataBinding?.daysLayout?.headerTitleTv?.text=getString(R.string.no_of_working)
        viewDataBinding?.leaveLayout?.headerTitleTv?.text=getString(R.string.no_of_days_leave_taken)

    }


    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(context!!,clazz, null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Singleton.isDashBoardFragmentVisible =true
        mToobarManagner = context as IToolbar
    }
}
