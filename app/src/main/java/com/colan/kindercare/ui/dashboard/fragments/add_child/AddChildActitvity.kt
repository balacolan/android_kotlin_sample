package com.colan.kindercare.ui.dashboard.fragments.add_child

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityAddChildActitvityBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.dashboard.DashboardVM
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class AddChildActitvity : BaseActivity<ActivityAddChildActitvityBinding,DashboardVM>(),BaseNavigator {

    override fun getBindingVariable(): Int {
        return  BR.dashboardVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_child_actitvity
    }

    override fun getViewModel(): DashboardVM {
        return ViewModelProviders.of(this).get(DashboardVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.add_child)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        val genderList = this.getResources().getStringArray(R.array.gender_arrays)
        val classList = this.getResources().getStringArray(R.array.class_array)
        val sectionList = this.getResources().getStringArray(R.array.section_array)
        mViewModel?.mGenderList?.addAll(genderList)
        mViewModel?.mClassList?.addAll(classList)
        mViewModel?.mSectionList?.addAll(sectionList)
    }


    override fun onClickView(v: View?) {
        when(v?.id){
            R.id.nav_icon_iv->{
                onBackPressed()
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

}
