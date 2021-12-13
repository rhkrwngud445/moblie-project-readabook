package com.book.readabook.feature.record

import android.media.CamcorderProfile.getAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.feature.tts.TTSViewModel
import com.book.readabook.global.Application
import com.book.readabook.model.data.RecordData
import com.book.readabook.repository.RecordRepository

class RecordViewModel(application: Application) : AndroidViewModel(application){

    private val items = RecordRepository(application).getAll()
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RecordViewModel(application) as T
        }
    }
    fun getAll() : LiveData<List<RecordData>> {
        return items
    }


}