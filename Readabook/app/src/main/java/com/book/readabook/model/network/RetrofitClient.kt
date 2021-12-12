package com.book.readabook.model.network

import android.util.Log
import com.book.readabook.global.Application
import com.book.readabook.model.network.api.KakaoOcrApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient{

    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()!!).build()

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor? {
        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.e("MyOCRData :", message + "")
            }
        })
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(Application.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val ApiService : KakaoOcrApi = retrofit.create(KakaoOcrApi::class.java)
}