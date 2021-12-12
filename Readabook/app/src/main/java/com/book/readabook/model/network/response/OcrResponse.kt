package com.book.readabook.model.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OcrResponse{
    @SerializedName("result")
    @Expose
    val result : ArrayList<Boxes>? = null
}

class Boxes{
    @SerializedName("recognition_words")
    @Expose
    val recognition_words : List<String?>? = null
}