package com.colan.kindercare.ui.before_login.forgot_password

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityForgotBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.before_login.BeforeLoginViewModel
import com.colan.kindercare.ui.before_login.login.LoginActivity
import com.colan.kindercare.ui.before_login.resetPassword.PasswordResetActivity


class ForgotPasswordActivity : BaseActivity<ActivityForgotBinding, BeforeLoginViewModel>(), BaseNavigator {

    private lateinit var viewModel: BeforeLoginViewModel
    override fun getBindingVariable(): Int {
        return BR.beforeLoginVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forgot
    }

    override fun getViewModel(): BeforeLoginViewModel {
        viewModel = ViewModelProviders.of(this).get(BeforeLoginViewModel::class.java)
        return viewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        observeResponse()
    }


    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
        viewModel.forgotPasswordResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                viewModel.mShowProgressBar.value = false
                                Toast.makeText(this, "Reset link has been sent to your mail", Toast.LENGTH_SHORT).show()
                                goTo(PasswordResetActivity::class.java,null)
                                finish()
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
        when(v!!.id){
            R.id.login_txt ->{
                goTo(LoginActivity::class.java,null)
            }

            R.id.submit_btn->{
                viewModel.doApiCallForgotPassword()

            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }
}
