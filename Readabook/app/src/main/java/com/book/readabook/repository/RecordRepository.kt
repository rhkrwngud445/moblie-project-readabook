package com.book.readabook.repository

import androidx.lifecycle.LiveData
import com.book.readabook.global.Application
import com.book.readabook.model.data.RecordData
import com.book.readabook.model.db.AppDatabase
import com.book.readabook.model.db.dao.RecordDAO

class RecordRepository(application: Application) {
    private val recordDao : RecordDAO
    private val recordList : LiveData<List<RecordData>>

    init{
        var db = AppDatabase.getInstance(application)
        recordDao = db!!.recentSearchDao()
        recordList = db.recentSearchDao().getAll()
    }
    fun insert(recentSearch : RecordData) {
        recordDao.insert(recentSearch)
    }

    fun delete(recentSearch : RecordData){
        recordDao.delete(recentSearch)
    }

    fun getAll(): LiveData<List<RecordData>> {
        return recordDao.getAll()
    }

}