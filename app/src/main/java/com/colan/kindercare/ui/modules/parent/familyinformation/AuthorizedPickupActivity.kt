package com.colan.kindercare.ui.modules.parent.familyinformation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.databinding.ActivityAutorizedPickupBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import kotlinx.android.synthetic.main.custom_toolbar.view.*


class AuthorizedPickupActivity : BaseActivity<ActivityAutorizedPickupBinding,FamilyInformationVM>(),BaseNavigator {
    override fun getBindingVariable(): Int {
       return BR.familyInformationVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_autorized_pickup
    }

    override fun getViewModel(): FamilyInformationVM {
        return ViewModelProviders.of(this).get(FamilyInformationVM::class.java)
    }

    override fun onClickView(v: View?) {
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.authorized_pickup)
        getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        getViewDataBinding().txtRelationship.setOnTouchListener({ v, event ->
            mViewModel?.hasSpinnerFocused?.set(true)
            false
        })
    }

}
