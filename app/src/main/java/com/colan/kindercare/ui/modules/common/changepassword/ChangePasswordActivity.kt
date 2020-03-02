package com.colan.kindercare.ui.modules.common.changepassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityChangePasswordBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.common.profile.ProfileVM
import com.colan.kindercare.utils.AsteriskPasswordTransformationMethod
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.navigateTo
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import org.koin.android.ext.android.inject


class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding, ProfileVM>(),
    BaseNavigator {
    private val sharedPreferences: ISharedPreferenceService by inject()
    override fun getBindingVariable(): Int {
        return BR.profileVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun getViewModel(): ProfileVM {
        return ViewModelProviders.of(this).get(ProfileVM::class.java)
    }

    override fun onClickView(v: View?) {
        when(v?.id) {
            R.id.cancel_btn -> {
              onBackPressed()
            }
            R.id.updatePassword_btn->{
                mViewModel?.doCallUpdatePasswordAPI()
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)

      //  getViewDataBinding().toolbar.title_toolbar.text=getString(R.string.change_password)
      //  getViewDataBinding().toolbar.nav_icon_iv.setOnClickListener { onBackPressed() }
        mViewModel?.setUpUserCredentials()
        getViewDataBinding().oldPasswordEt.transformationMethod=AsteriskPasswordTransformationMethod()
        getViewDataBinding().newPasswordEt.transformationMethod=AsteriskPasswordTransformationMethod()
        getViewDataBinding().confirmPasswordEt.transformationMethod=AsteriskPasswordTransformationMethod()
        observeResponse()
        setUpProfileImage()

    }
    private fun setUpProfileImage() {
        when {
            sharedPreferences.getStringValue(Constants.PROFILE_IMAGE).isNotEmpty() -> Glide.with(this)
                .asBitmap().load(sharedPreferences.getStringValue(Constants.PROFILE_IMAGE))
                .placeholder(R.drawable.ic_profile_pic)
                .into(getViewDataBinding().toolbar.profileChildIv)
        }

    }



    private fun observeResponse() {
        mViewModel?.mShowProgressBar?.observeEvent(this, androidx.lifecycle.Observer {
            when {


                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })

        mViewModel?.updatePasswordResponse?.observe(this,androidx.lifecycle.Observer {
            if (it != null) {
                mViewModel?.mShowProgressBar?.value = false
                try {
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                }catch (e: java.lang.Exception) {
                    mViewModel?.mShowProgressBar?.value = false
                    Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun validateOldNewPass(){
        val old = getViewDataBinding().oldPasswordEt
        val new = getViewDataBinding().newPasswordEt
        if (old != new){

        }

    }
}
