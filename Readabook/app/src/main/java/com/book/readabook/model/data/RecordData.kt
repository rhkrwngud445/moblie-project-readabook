package com.book.readabook.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recordData")
data class RecordData(var text : String){
    @PrimaryKey(autoGenerate = true) var id: Int = 0

}