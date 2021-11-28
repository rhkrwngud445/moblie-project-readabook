package com.book.readabook.feature.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.R
import com.book.readabook.databinding.ActivityMainBinding
import com.book.readabook.databinding.ActivityRecordBinding
import com.book.readabook.feature.main.MainViewModel

class RecordActivity : AppCompatActivity(){
    private var binding : ActivityRecordBinding? = null

    private val viewModel : RecordViewModel by lazy{
        ViewModelProvider(this).get(RecordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityRecordBinding>(
            this, R.layout.activity_record
        ).apply {
            lifecycleOwner = this@RecordActivity
            recordVm = viewModel
        }

    }
}