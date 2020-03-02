package com.colan.kindercare.data.remote.client

import android.util.Log
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.colan.kindercare.utils.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import java.io.IOException


class AuthTokenInterceptor(private val sharedPreferences: ISharedPreferenceService)  : Interceptor, KoinComponent {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val builder = request.newBuilder()
        builder.header("Accept", "application/json")
        builder.header("API_VERSION", "1.0")
        builder.header("DEVICE_TYPE", "Android")
        val token = sharedPreferences.getStringValue(Constants.AUTHORIZATION_KEY)
        setAuthHeader(builder, token)
        request = builder.build()
        val response = chain.proceed(request)         // Sends the request (Original w/ Auth.)
        //------------------- 401 --- 401 --- UNAUTHORIZED --- 401 --- 401 -------------------------
        if (response.code() == RESPONSE_UNAUTHORIZED_401) { //If unauthorized (Token expired)...
            Log.w(TAG_THIS, "Request responses code: " + response.code())


        } else {
            //------------------- 200 --- 200 --- AUTHORIZED --- 200 --- 200 -----------------------
            Log.w(TAG_THIS, "Request responses code: " + response.code())
        }
        return response
    }

    // Sets/Adds the authentication header to current request builder.-----------------------------|
    private fun setAuthHeader(builder: Request.Builder, token: String?) {
        Log.i(TAG_THIS, "Setting authentication header...")
        if (token != null) {
            builder.header("Authorization", String.format("Bearer %s", token))
        }

        Log.w(TAG_THIS, "Current Auth Token = " + sharedPreferences.getStringValue(Constants.AUTHORIZATION_KEY) )
        Log.w(TAG_THIS, "Current Refresh Token = " +sharedPreferences.getStringValue(Constants.REFRESH_TOKEN) )
    }

    companion object {
        private val TAG_THIS = AuthTokenInterceptor::class.java.simpleName
        private const val RESPONSE_UNAUTHORIZED_401 = 401
        @Deprecated("", ReplaceWith("\"Nullix\""))
        private fun bodyToString(request: Request): String {
            return "Nullix"
        }
    }

}
