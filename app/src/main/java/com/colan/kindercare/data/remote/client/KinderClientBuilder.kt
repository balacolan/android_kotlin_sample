package com.colan.kindercare.data.remote.client

import android.os.Debug
import com.colan.kindercare.BuildConfig
import com.colan.kindercare.data.local.sharedPreference.ISharedPreferenceService
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.*


class KinderClientBuilder(private val interceptor: Interceptor, private val sharedPreferences: ISharedPreferenceService) : KoinComponent,IKinderClientBuilder {

    private val API_BASE_URL = BuildConfig.BASE_URL
    private val CONNECT_TIMEOUT = 15
    private val READ_TIMEOUT = 60
    private val WRITE_TIMEOUT = 60


    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private val builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))



    private fun createClientAuth(): OkHttpClient {
        //ADD DISPATCHER WITH MAX REQUEST TO 1
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .build()

        return client
    }


    private val client: OkHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher())
        .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(interceptor)
        .build()

    private val retrofit: Retrofit = builder.client(createClientAuth()).build()

    init {
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
    }


    override fun getClient() = createClientAuth()

    override fun getBuilder() = builder

    override fun getRetrofit()= retrofit
}