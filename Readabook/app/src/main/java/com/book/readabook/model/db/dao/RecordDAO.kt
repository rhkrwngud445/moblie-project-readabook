package com.book.readabook.model.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.book.readabook.model.data.RecordData

@Dao
interface RecordDAO {
    @Query("SELECT * FROM recordData ORDER BY id DESC")
    fun getAll() : LiveData<List<RecordData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recordData: RecordData)

    @Delete
    fun delete(recordData: RecordData)
}