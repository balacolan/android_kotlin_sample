package com.colan.kindercare.ui.modules.common.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityProfileBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.modules.common.changepassword.ChangePasswordActivity
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.navigateTo
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.inject

class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileVM>(), BaseNavigator {


    private val sharedPreferences:ISharedPreferenceService  by inject()

    override fun getBindingVariable(): Int {
        return BR.profileVM
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }
    override fun getViewModel(): ProfileVM {
        return ViewModelProviders.of(this).get(ProfileVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.setNavigator(this)
        mViewModel?.mScreenTitle?.set("Profile")
        getObserver()
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


    private fun getObserver() {
        mViewModel?.getUserProfileResponse?.observe(this, Observer {
            when(it.status){
                Status.SUCCESS->{
                    mViewModel?.getUserProfileJob?.cancel()

                    it.data?.data.let {
                        mViewModel?.saveUserDetail(it?.firstname,it?.email,it?.contact,it?.dateOfBirth,it?.address)
                        mViewModel?.mUserName?.set(it?.firstname)
                        mViewModel?.mUserMailId?.set(it?.email)
                        mViewModel?.mUserMobileNumber?.set(it?.contact)
                        sharedPreferences.setStringValue(Constants.PROFILE_IMAGE,Constants.PROFILE_IMAGE_PATH+it?.profile)
                        mViewModel?.mUserDOB?.set(it?.dateOfBirth)
                        when {
                            it?.gender==2 -> mViewModel?.mUserGender?.set("Female")
                            else -> mViewModel?.mUserGender?.set("Male")
                        }
                        mViewModel?.mUserAddress?.set(it?.address as String?)
                        mViewModel?.mUserPassword?.set("password")
                        setUpProfileImage()
                    }
                }
                Status.ERROR->{

                }
                else -> {

                }
            }

        })
    }


    override fun onClickView(v: View?) {
        when (v!!.id) {
            R.id.edit_profile_btn -> {
                goTo(EditProfileActivity::class.java, null)
            }
            R.id.status_tv -> {
                goTo(ChangePasswordActivity::class.java, null)
            }
            R.id.nav_icon_iv->{
                finish()
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        navigateTo(this, clazz, null)
    }

    override fun onResume() {
        super.onResume()
        mViewModel?.getUserProfile()

    }


}
