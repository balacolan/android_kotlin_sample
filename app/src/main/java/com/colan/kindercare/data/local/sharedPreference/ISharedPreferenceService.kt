package com.colan.kindercare.data.local.sharedPreference

interface ISharedPreferenceService {
    fun getStringValue(key: String): String
    fun getStringValue(key: String, defaultValue: String): String
    fun getBooleanValue(key: String): Boolean
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean
    fun getIntegerValue(key: String): Int
    fun getFloatValue(key: String): Float
    fun setStringValue(key: String, value: String)
    fun setBooleanValue(key: String, value: Boolean)
    fun setIntegerValue(key: String, value: Int)
    fun setFloatValue(key: String, value: Float)
    fun containsKey(key: String): Boolean
    fun clearAllValues()
}