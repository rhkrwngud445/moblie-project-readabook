package com.book.readabook.repository

import com.book.readabook.model.network.RetrofitClient
import okhttp3.MultipartBody

class OcrRepository {
    private val client = RetrofitClient.ApiService

    suspend fun getTextOcr(authorization : String, file : MultipartBody.Part) = client.getTextOcr(authorization,file)
}