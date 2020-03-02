package com.colan.kindercare.ui.before_login.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.BuildConfig
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityLoginBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.before_login.BeforeLoginViewModel
import com.colan.kindercare.ui.before_login.forgot_password.ForgotPasswordActivity
import com.colan.kindercare.ui.dashboard.DashboardActivity
import com.colan.kindercare.utils.AsteriskPasswordTransformationMethod
import com.colan.kindercare.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent


class LoginActivity : BaseActivity<ActivityLoginBinding, BeforeLoginViewModel>(), BaseNavigator,
    KoinComponent {

    private val iSharedPreferenceService:ISharedPreferenceService by inject()

    private lateinit var viewModel: BeforeLoginViewModel

    override fun getBindingVariable(): Int {
        return BR.beforeLoginVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): BeforeLoginViewModel {
        viewModel = ViewModelProviders.of(this).get(BeforeLoginViewModel::class.java)
        return viewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        version_tv.text = BuildConfig.VERSION_NAME
        getViewDataBinding().passwordEt.setTransformationMethod(AsteriskPasswordTransformationMethod())
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
        viewModel.loginResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {

                        try {
                            if (it.data!=null) {
                                viewModel.mShowProgressBar.value = false
                                iSharedPreferenceService.setBooleanValue(Constants.ALREADY_LOGED_IN,true)
                                iSharedPreferenceService.setStringValue(Constants.INSTITUTE_ID,""+it.data.instituteId)
                                iSharedPreferenceService.setStringValue(Constants.USER_NAME,""+it.data.name)
                                iSharedPreferenceService.setStringValue(Constants.USER_TYPE,""+it.data.userTypeName)
                                iSharedPreferenceService.setStringValue(Constants.USER_TYPE_ID,""+it.data.userType)
                                iSharedPreferenceService.setStringValue(Constants.AUTHORIZATION_KEY,""+it.data.token)
                                iSharedPreferenceService.setStringValue(Constants.AUTHORIZATION_TYPE,""+it.data.tokenType)
                                iSharedPreferenceService.setStringValue(Constants.PROFILE_IMAGE,""+it.data.profile)
                                iSharedPreferenceService.setStringValue(Constants.USER_ID,""+it.data.userId)
                                iSharedPreferenceService.setStringValue(Constants.STUDENT_SIGN_IN,""+it.data.permissions?.studentSignin)
                                iSharedPreferenceService.setStringValue(Constants.TEACHER_SIGN_IN,""+it.data.permissions?.teacherSignin)
                                iSharedPreferenceService.setStringValue(Constants.ADMIN_LOGIN,""+it.data.permissions?.adminLogin)
                                iSharedPreferenceService.setStringValue(Constants.VIEW_SALARY,""+it.data.permissions?.viewSalary)
                                iSharedPreferenceService.setStringValue(Constants.EDIT_STAFF_ATTENDANCE,""+it.data.permissions?.editStaffAttendance)
                                Toast.makeText(this, "" + it.data.userTypeName, Toast.LENGTH_SHORT).show()
                                goTo(DashboardActivity::class.java, null)
                            }else {
                                viewModel.mShowProgressBar.value = false
                                Toast.makeText(this, "Error getting User Information", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } catch (e: java.lang.Exception) {
                            viewModel.mShowProgressBar.value = false
                            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    Status.ERROR, Status.FAILURE -> {
                        viewModel.mShowProgressBar.value = false
                        putToast(""+it.message)
                    }
                }

            }

        })
    }

    override fun onClickView(v: View?) {
        when (v!!.id) {
            R.id.login_btn -> {
                viewModel.doApiCallLogin()
                //goTo(DashboardActivity::class.java, null)
            }
            R.id.forgotPassword_txt -> {
                goTo(ForgotPasswordActivity::class.java, null)
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }

}
