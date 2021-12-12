package com.book.readabook.feature.tts

import android.util.Log
import androidx.lifecycle.ViewModel
import com.book.readabook.repository.OcrRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class TTSViewModel : ViewModel(){
    fun getTextOcr(authorization : String, file : MultipartBody.Part){
        CoroutineScope(Dispatchers.IO).launch {
            OcrRepository().getTextOcr(authorization,file).let {
                if(it.isSuccessful){
                    Log.d("OCRText","success")
                }
                else{
                    Log.d("OCRText",it.code().toString())
                    Log.d("OCRText",it.message().toString())
                }
            }
        }
    }
}