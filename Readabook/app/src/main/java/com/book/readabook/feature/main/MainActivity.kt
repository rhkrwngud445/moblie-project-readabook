package com.book.readabook.feature.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.R
import com.book.readabook.databinding.ActivityMainBinding
import com.book.readabook.feature.record.RecordActivity
import com.book.readabook.feature.tts.TTSActivity

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        ).apply {
            lifecycleOwner = this@MainActivity
            mainVm = viewModel
        }
        initClickListener()
    }

    private fun initClickListener() {
        binding!!.tvMainStart.setOnClickListener {
            startActivity(Intent(this, TTSActivity::class.java))
        }
        binding!!.tvMainRecord.setOnClickListener {
            startActivity(Intent(this, RecordActivity::class.java))
        }
    }
}