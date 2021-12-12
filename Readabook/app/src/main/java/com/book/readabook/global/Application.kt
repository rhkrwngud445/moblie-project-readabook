package com.book.readabook.global

import android.app.Application

class Application : Application(){

    companion object{
        var BASE_URL = "https://dapi.kakao.com/"
    }

    override fun onCreate() {
        super.onCreate()
    }
}