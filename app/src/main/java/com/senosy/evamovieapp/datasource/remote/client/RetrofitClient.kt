package com.senosy.evamovieapp.datasource.remote.client

import androidx.viewbinding.BuildConfig
import com.github.simonpercic.oklog3.OkLogInterceptor
import com.senosy.evamovieapp.Constants
import com.squareup.moshi.Moshi
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Client class that provides [Retrofit] client.
 */
class RetrofitClient(private val apiKey: String) {

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private fun getOkLogInterceptor(): OkLogInterceptor = OkLogInterceptor.builder().build()

    private fun getApiKeyInterceptor(): AuthenticationInterceptor =
        AuthenticationInterceptor(apiKey = apiKey)

    private fun getMoshi(): Moshi = Moshi.Builder().build()

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            addInterceptor(getOkLogInterceptor())
        }
        addInterceptor(getHttpLoggingInterceptor())
        addInterceptor(getApiKeyInterceptor())
    }.build()

    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.PROD_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
        .client(provideOkHttpClient())
        .build()
    companion object{
        fun getRetrofit(apiKey: String): Retrofit {
            return RetrofitClient(apiKey = apiKey).provideRetrofit()
        }
    }
}