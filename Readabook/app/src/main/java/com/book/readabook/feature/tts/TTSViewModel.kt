package com.book.readabook.feature.tts

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.global.Application
import com.book.readabook.model.data.RecordData
import com.book.readabook.model.network.response.OcrResponse
import com.book.readabook.repository.OcrRepository
import com.book.readabook.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class TTSViewModel(application: Application) : AndroidViewModel(application){

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TTSViewModel(application) as T
        }
    }
    private val recordRepository = RecordRepository(application)

    private val response = MutableLiveData<OcrResponse>()

    val get_response : MutableLiveData<OcrResponse> get() = response

    fun getTextOcr(authorization : String, file : MultipartBody.Part){
        CoroutineScope(Dispatchers.IO).launch {
            OcrRepository().getTextOcr(authorization,file).let {
                if(it.isSuccessful){
                    Log.d("OCRText","success")
                    get_response.postValue(it.body())
                }

                else{
                    Log.d("OCRText",it.code().toString())
                    Log.d("OCRText",it.message().toString())
                }
            }
        }
    }

    fun insert(recordData : RecordData){
        recordRepository.insert(recordData)
    }
}