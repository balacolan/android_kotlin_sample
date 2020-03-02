package com.colan.kindercare

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.multidex.MultiDexApplication
import com.colan.kindercare.bindiingUtils.BaseDataBindingComponent
import com.colan.kindercare.di.KoinCoreModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KinderApplication : MultiDexApplication() {

    companion object {
        lateinit  var appContext: Context
        var instance: KinderApplication = KinderApplication()
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Stetho.initializeWithDefaults(this)
        DataBindingUtil.setDefaultComponent(BaseDataBindingComponent())
        val moduleList = listOf(KoinCoreModule().koinCoreModule)
        startKoin{
            androidContext(this@KinderApplication)
            modules(moduleList)
        }
    }
}