package com.colan.kindercare.data.local.sharedPreference

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.core.KoinComponent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SharedPreferenceImp (androidApplication: Application): KoinComponent, ISharedPreferenceService {

    private val PREFERENCE_NAME = "preferenceUTEAL"
    private var shared: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    init {
        this.shared = androidApplication.getSharedPreferences(PREFERENCE_NAME,
            Context.MODE_PRIVATE)
    }

    override fun getStringValue(key: String): String {
        return shared.getString(key, "")!!
    }

    override fun getStringValue(key: String, defaultValue: String): String {
        return shared.getString(key, defaultValue)!!
    }

    override fun getBooleanValue(key: String): Boolean {
        return shared.getBoolean(key, false)
    }

    override fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return shared.getBoolean(key, defaultValue)
    }

    override fun getIntegerValue(key: String): Int {
        return shared.getInt(key, 0)
    }

    override fun getFloatValue(key: String): Float {
        return shared.getFloat(key, 0f)
    }

    @SuppressLint("CommitPrefEdits")
    override fun setStringValue(key: String, value: String) {
        try {
            editor = shared.edit()
            editor!!.putString(key, value)
            editor!!.apply()
        } catch (e: Exception) {
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setBooleanValue(key: String, value: Boolean) {
        try {
            editor = shared.edit()
            editor!!.putBoolean(key, value)
            editor!!.apply()
        } catch (e: Exception) {
        }
    }

    @SuppressLint("CommitPrefEdits")
    override fun setIntegerValue(key: String, value: Int) {
        try {
            editor = shared.edit()
            editor!!.putInt(key, value)
            editor!!.apply()
        } catch (e: Exception) {
        }
    }

    override fun setFloatValue(key: String, value: Float) {
        try {
            editor = shared.edit()
            editor!!.putFloat(key, value)
            editor!!.apply()
        } catch (e: Exception) {
        }
    }

    override fun clearAllValues() {
        try {
            shared.edit().clear().apply()
        } catch (e: Exception) {
        }
    }

    override fun containsKey(key: String): Boolean {
        return shared.contains(key)
    }
}