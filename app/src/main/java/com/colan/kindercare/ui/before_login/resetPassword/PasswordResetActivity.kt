package com.colan.kindercare.ui.before_login.resetPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.remote.data.Status
import com.colan.kindercare.databinding.ActivityPasswordResetBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.before_login.BeforeLoginViewModel
import com.colan.kindercare.utils.AsteriskPasswordTransformationMethod

class PasswordResetActivity : BaseActivity<ActivityPasswordResetBinding, BeforeLoginViewModel>(),
    BaseNavigator {

    private lateinit var viewModel: BeforeLoginViewModel


    override fun getBindingVariable(): Int {
        return BR.beforeLoginVM
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_password_reset
    }

    override fun getViewModel(): BeforeLoginViewModel {
        viewModel = ViewModelProviders.of(this).get(BeforeLoginViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        getViewDataBinding().etCreatePassword.transformationMethod =
            AsteriskPasswordTransformationMethod()
        getViewDataBinding().etConfirmPassword.transformationMethod =
            AsteriskPasswordTransformationMethod()
        observeResponse()
    }

    private fun observeResponse() {
        viewModel.mShowProgressBar.observeEvent(this, Observer {
            when {
                it -> showLoadingIndicator()
                else -> dismissLoadingIndicator()
            }
        })
        viewModel.resetPasswordResponse.observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        viewModel.mShowProgressBar.value = true
                    }
                    Status.SUCCESS -> {
                        try {
                            if (it.data!=null) {
                                viewModel.mShowProgressBar.value = false
                                Toast.makeText(this, "Password Updated Successfully", Toast.LENGTH_SHORT).show()
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
            R.id.submit_btn->{
                viewModel.doApiCallResetPassword()
            }
        }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)
    }
}
