package com.book.readabook.feature.record

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.book.readabook.R
import com.book.readabook.databinding.ActivityMainBinding
import com.book.readabook.databinding.ActivityRecordBinding
import com.book.readabook.feature.main.MainViewModel
import com.book.readabook.feature.tts.TTSViewModel
import com.book.readabook.global.Application

class RecordActivity : AppCompatActivity(){
    private var binding : ActivityRecordBinding? = null
    private lateinit var adapter : RecordRvAdapter

    private val viewModel by lazy {
        ViewModelProvider(this, RecordViewModel.Factory(application as Application)).get(RecordViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityRecordBinding>(
            this, R.layout.activity_record
        ).apply {
            lifecycleOwner = this@RecordActivity
            recordVm = viewModel
        }

        initRecyclerView()
        initObserve()
    }
// adapter.setList
    private fun initRecyclerView(){
        var linearLayoutManager = LinearLayoutManager(this)
        adapter = RecordRvAdapter()
        binding!!.rvRecord.layoutManager = linearLayoutManager
    }
    private fun initObserve(){
        with(viewModel){
            getAll().observe(this@RecordActivity, Observer {
                adapter.setList(it)
                adapter.notifyDataSetChanged()
                binding!!.rvRecord.adapter=adapter
                binding!!.rvRecord.adapter!!.notifyDataSetChanged()
            })
        }
    }
}