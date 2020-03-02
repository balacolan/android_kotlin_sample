package com.colan.kindercare.ui.dashboard.fragments


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.FragmentDashboardBinding
import com.colan.kindercare.ui.base.BaseFragment
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.DashboardVM
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.dashboard.fragments.all_activity.classRoom.ClassRoomFragment
import com.colan.kindercare.ui.dashboard.fragments.all_activity.meal.MealFragment
import com.colan.kindercare.ui.dashboard.fragments.all_activity.photos.PhotosFragment
import com.colan.kindercare.utils.Singleton.isDashBoardFragmentVisible
import com.colan.kindercare.utils.StringToDateConvert
import com.colan.kindercare.utils.decrementDateByOne
import com.colan.kindercare.utils.incrementDateByOne
import com.colan.kindercare.utils.toString

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : BaseFragment<FragmentDashboardBinding,DashboardVM>(),BaseNavigator {

    private lateinit var mToobarManagner: IToolbar

    override val bindingVariable: Int
        get() = BR.dashboardVM
    override val layoutId: Int
        get() = R.layout.fragment_dashboard
    override val viewModel: DashboardVM
        get() = ViewModelProviders.of(this).get(DashboardVM::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mToobarManagner.changeToolbarTitle(getString(R.string.dashboard))
        showFragment(PhotosFragment())
    }

    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.photos_iv->{
                showFragment(PhotosFragment())
            }
            R.id.meal_iv->{
                showFragment(MealFragment())
            }
            R.id.classroom_iv->{
                showFragment(ClassRoomFragment())
            }
            R.id.date_previous_iv->{
                viewModel.currentDate.set(viewModel.currentDate.get()?.let { StringToDateConvert(it) }?.let { decrementDateByOne(it) }?.toString("dd MMMM yyyy"))
            }
            R.id.date_next_iv->{
                viewModel.currentDate.set(viewModel.currentDate.get()?.let { StringToDateConvert(it) }?.let { incrementDateByOne(it) }?.toString("dd MMMM yyyy"))
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isDashBoardFragmentVisible=true
        mToobarManagner = context as IToolbar
    }

    override fun onDetach() {
        super.onDetach()
        isDashBoardFragmentVisible=false
    }

    private fun showFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.dashboard_container, fragment)?.commit()
    }

}
