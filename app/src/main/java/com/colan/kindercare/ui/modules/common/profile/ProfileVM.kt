package com.colan.kindercare.ui.modules.common.profile

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.data.remote.data.BaseResponse
import com.colan.kindercare.data.remote.data.Resource
import com.colan.kindercare.data.remote.data.response.profile.UserProfileResponse
import com.colan.kindercare.data.repository.user.IUserControllerRepository
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.base.BaseViewModel
import com.colan.kindercare.utils.Constants
import com.colan.kindercare.utils.SingleLiveData
import com.colan.kindercare.utils.validateEmail
import com.colan.kindercare.utils.validateMobile
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

class ProfileVM(application: Application) : BaseViewModel<BaseNavigator>(application),KoinComponent {

    private val userControllerRepository: IUserControllerRepository by inject()
    private val sharedPreferences:ISharedPreferenceService  by inject()
    val mScreenTitle = ObservableField ("")
    val mUserName = ObservableField("")
    val mUserName1 = ObservableField("")
    val mUserMailId = ObservableField("")
    val mUserMailId1 = ObservableField("")
    val mUserMobileNumber = ObservableField("")
    val mUserMobileNumber1 = ObservableField("")
    val mUserDOB = ObservableField("")
    val mUserGender = ObservableField("1")
    val mUserAddress = ObservableField("")
    val mUserPassword = ObservableField("")
    val mOldPassword = ObservableField("")
    val mNewPassword = ObservableField("")
    val mConfirmPassword = ObservableField("")
    val getUserProfileResponse = MutableLiveData<Resource<BaseResponse<UserProfileResponse>>>()
    val updateProfileResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val updatePasswordResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    val updateProfileImageResponse = MutableLiveData<Resource<BaseResponse<Nothing>>>()
    var getUserProfileJob: Job? = null
    val profilePicture = ObservableField("")
    val mShowProgressBar = SingleLiveData<Boolean>()
    var outputFileUri: Uri? = null
    var resultUri: Uri? = null
    var sdImageMainDirectory: File? = null
    var currentPicture: String? = null
    var encodedImage: String = ""
    var file: File? = null

    init {

    }

    fun onClickAction(view: View?) {
        getNavigator().onClickView(view)
    }

    fun getUserProfile(){
        GlobalScope.launch{
            getUserProfileJob=async(Dispatchers.IO) {
                userControllerRepository.getUserProfile(sharedPreferences.getStringValue(Constants.USER_ID),getUserProfileResponse)
            }
        }

    }

    fun saveUserDetail(username: String?, email: String?, contact: String?, dateOfBirth: String?, address: Any?) {
        sharedPreferences.setStringValue(Constants.USER_NAME,""+username)
        sharedPreferences.setStringValue(Constants.USER_EMAIL,""+email)
        sharedPreferences.setStringValue(Constants.USER_CONTACT,""+contact)
        sharedPreferences.setStringValue(Constants.USER_DOB,""+dateOfBirth)
        sharedPreferences.setStringValue(Constants.USER_ADDRESS,""+address)
    }

    fun setUpUserCredentials() {
        mUserName.set(sharedPreferences.getStringValue(Constants.USER_NAME))
        mUserName1.set(sharedPreferences.getStringValue(Constants.USER_NAME))
        mUserMailId.set(sharedPreferences.getStringValue(Constants.USER_EMAIL))
        mUserMailId1.set(sharedPreferences.getStringValue(Constants.USER_EMAIL))
        mUserMobileNumber.set(sharedPreferences.getStringValue(Constants.USER_CONTACT))
        mUserMobileNumber1.set(sharedPreferences.getStringValue(Constants.USER_CONTACT))
        mUserDOB.set(sharedPreferences.getStringValue(Constants.USER_DOB))
        mUserAddress.set(sharedPreferences.getStringValue(Constants.USER_ADDRESS))
    }

    fun doCallUpdateProfileAPI() {
        if(validateCredentials()){
            mShowProgressBar.value=true
            userControllerRepository.updateProfile(
                "PUT",
                mUserName.get().toString(),
                mUserGender.get().toString(),
                mUserMailId.get().toString(),
                mUserDOB.get().toString(),
                mUserMobileNumber.get().toString(),
                mUserAddress.get().toString(),
                updateProfileResponse
            )
        }

    }


    fun doCallUpdatePasswordAPI() {
        if(validatePassworCredentials()){
            mShowProgressBar.value=true
            userControllerRepository.updatePassword(
                "PUT",
                mUserName.get().toString(),
                mUserMailId.get().toString(),
                mOldPassword.get().toString(),
                mNewPassword.get().toString(),
                mConfirmPassword.get().toString(),
                updatePasswordResponse
            )
        }
    }

    fun doCallUpdateProfilePictureAPI() {
        Log.d("file",""+file)
        userControllerRepository.updateProfileImage("PUT",file,updateProfileImageResponse)
    }

    private fun validatePassworCredentials(): Boolean {
        when{
            mOldPassword.get()!!.isEmpty() -> putToast("Please enter your old password")
            mConfirmPassword.get()!!.isEmpty() -> putToast("Please enter your Confirm Password")
            mNewPassword.get() != mConfirmPassword.get() -> {
                putToast("Password and Confirm Password should be same")
            }
            /*mNewPassword.get()!! == mOldPassword.get() ->{
                putToast("Old password and new password should not be same")
            }*/

            else -> return true
        }
        return false
    }

    private fun validateCredentials(): Boolean {
       when{
           mUserName.get()!!.isEmpty() -> putToast("Please enter User name")
        //   mUserGender.get()!!.isEmpty() -> putToast("Please enter your Gender")
           mUserMailId.get()!!.isEmpty() -> putToast("Please enter your Email Id")
           !validateEmail(mUserMailId.get()!!) -> {
               putToast("Please enter your Valid email address")
           }
           mUserMobileNumber.get()!!.isEmpty() -> putToast("Please enter your Mobile No")
           !validateMobile(mUserMobileNumber.get()!!)-> putToast("Please enter valid Mobile No")


           else -> return true
       }
        return false
    }



}