package com.book.readabook.model.network.api

import com.book.readabook.model.network.response.OcrResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface KakaoOcrApi{

    @Multipart
    @POST("/v2/vision/text/ocr")
    suspend fun getTextOcr(@Header("Authorization") authorization : String,
                           @Part file : MultipartBody.Part) : Response<OcrResponse>
}