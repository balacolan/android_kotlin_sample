package com.colan.kindercare.ui.before_login.splashscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.colan.kindercare.BR
import com.colan.kindercare.R
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.databinding.ActivitySplashBinding
import com.colan.kindercare.ui.base.BaseActivity
import com.colan.kindercare.ui.base.BaseNavigator
import com.colan.kindercare.ui.before_login.BeforeLoginViewModel
import com.colan.kindercare.ui.before_login.login.LoginActivity
import com.colan.kindercare.ui.dashboard.DashboardActivity
import com.colan.kindercare.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class SplashActivity : BaseActivity<ActivitySplashBinding,BeforeLoginViewModel>(), BaseNavigator,KoinComponent {
    private lateinit var viewModel: BeforeLoginViewModel

    private val iSharedPreferenceService: ISharedPreferenceService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun onClickView(v: View?) {
     when(v!!.id){
         R.id.start_btn ->{
             if(iSharedPreferenceService.containsKey(Constants.ALREADY_LOGED_IN)){
                 if(iSharedPreferenceService.getBooleanValue(Constants.ALREADY_LOGED_IN)){
                     goTo(DashboardActivity::class.java,null)
                     finish()
                 }else{
                     goTo(LoginActivity::class.java,null)
                     finish()
                 }
             } else{
                 goTo(LoginActivity::class.java,null)
                 finish()
             }


         }
     }
    }

    override fun goTo(clazz: Class<*>, mExtras: Bundle?) {
        val intent = Intent(this, clazz)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit)

    }

    override fun getBindingVariable(): Int {
        return BR.beforeLoginVM


    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModel(): BeforeLoginViewModel {
        viewModel = ViewModelProviders.of(this).get(BeforeLoginViewModel::class.java)
        return viewModel
    }
}
