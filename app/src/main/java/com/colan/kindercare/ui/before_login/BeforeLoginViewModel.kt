package com.colan.kindercare.ui.before_login

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ForgotPasswordResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.LoginResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.ResetPasswordResponse
import com.colan.kindercare.data.remote.data.response.withoutTokenResponse.WithOutTokenBaseResponse
import com.colan.kindercare.data.repository.user.IUserControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.SingleLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject

class BeforeLoginViewModel(application: Application) : BaseViewModel<BaseNavigator>(application),
    KoinComponent
{
    private val userControllerRepository: IUserControllerRepository by inject()
    val userName = ObservableField("")
    val password = ObservableField("")
    //val userName = ObservableField("kcsuperadmin2@yopmail.com")
   // val password = ObservableField("sadmin")
    val createPassword = ObservableField("")
    val confirmPassword = ObservableField("")
    val otp = ObservableField("")
    val loginResponse = MutableLiveData<Resource<LoginResponse>>()
    val forgotPasswordResponse = MutableLiveData<Resource<ForgotPasswordResponse>>()
    val resetPasswordResponse = MutableLiveData<Resource<ResetPasswordResponse>>()
    var versionName = ObservableField("")
    val mShowProgressBar = SingleLiveData<Boolean>()
    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    private fun validateCredentials(): Boolean {
        when {
            userName.get()!!.isEmpty() -> putToast("Please enter your email address")
            password.get()!!.isEmpty() -> putToast("Please enter your Password")
            else -> return true
        }
        return false
    }

    fun doApiCallLogin() {
        if (validateCredentials()) {
            mShowProgressBar.value=true
            userControllerRepository.login(
                userName.get().toString(),
                password.get().toString().trim(),
                loginResponse
            )
        }
    }

    fun doApiCallForgotPassword() {
        if (validateResetPasswordCredentials()) {
            mShowProgressBar.value=true
            userControllerRepository.forgotPassword(
                userName.get().toString(),
                        forgotPasswordResponse
            )
        }
    }

    fun doApiCallResetPassword() {
        if (validateResetCredentials()) {
            mShowProgressBar.value=true
            userControllerRepository.resetPassword(
                createPassword.get().toString(),
                confirmPassword.get().toString(),
                otp.get().toString(),
                resetPasswordResponse
            )
        }
    }

    private fun validateResetCredentials(): Boolean {
        when {
            createPassword.get()!!.isEmpty() -> putToast("Please enter Create Passsword")
            confirmPassword.get()!!.isEmpty() -> putToast("Please enter your Confirm Password")
            !createPassword.get().equals(confirmPassword.get())->putToast("Passwor Mismatch")
            else -> return true
        }
        return false
    }


    private fun validateResetPasswordCredentials(): Boolean {
        when {
            userName.get()!!.isEmpty() -> putToast("Please enter your email address")

            else -> return true
        }
        return false
    }

}