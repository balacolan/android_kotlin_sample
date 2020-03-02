package com.colan.kindercare.ui.modules.common.dailyactivity.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityDailyDetailBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.IToolbar
import com.colan.kindercare.ui.modules.common.dailyactivity.DailyActivityVM
import com.colan.kindercare.ui.modules.teacher.dailyActivity.AllActivity.AllDailyCommonActivty
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.Singleton
import com.colan.kindercare.utils.bundle
import com.colan.kindercare.utils.navigateTo

class DailyActivityDetail : BaseActivity<ActivityDailyDetailBinding, DailyActivityVM>(),
    BaseNavigator, IToolbar {


    override fun getBindingVariable(): Int {
        return BR.dailyActivityDetail
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_daily_detail
    }

    override fun getViewModel(): DailyActivityVM {
        return ViewModelProviders.of(this).get(DailyActivityVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel!!.setNavigator(this)

        val FragmentDetail = intent.getStringExtra("FragmentDetail")

        when (FragmentDetail) {
            "Meal" -> {
                val fragment = MealFragment()
                showFragment(fragment)
                mViewModel!!.isReject.set(true)
            }
            "Nap" -> {
                val fragment = NapFragment()
                showFragment(fragment)
                mViewModel!!.isReject.set(true)
            }
            "Classroom" -> {
                val fragment = ClassRoomFragmentDailyActivity()
                showFragment(fragment)
            }
            "Medicine" -> {
                val fragment = MedicineFragment()
                showFragment(fragment)
            }
            "Incident" -> {
                val fragment = IncidentFragment()
                showFragment(fragment)
                mViewModel!!.isReject.set(true)
            }
        }
    }

    override fun changeNavigationIcon(drawableIcon: Int) {
        getViewDataBinding().navIconIv.setImageResource(drawableIcon)
    }

    override fun changeToolbarTitle(title: String) {
        getViewDataBinding().titleToolbar.text = title
    }

    override fun onClickView(v: View?) {
        when (v?.id) {
            R.id.nav_icon_iv -> {
                onBackPressed()
            }
            R.id.edit_pen_white->{
                bundle.putString(Constants.ACTIVITY_TYPE,"Meal")
                goTo(AllDailyCommonActivty::class.java, bundle)

            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, bundle)
    }
}
