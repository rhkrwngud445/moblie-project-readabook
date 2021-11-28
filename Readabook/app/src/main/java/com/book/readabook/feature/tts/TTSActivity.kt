package com.book.readabook.feature.tts

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.R
import com.book.readabook.databinding.ActivityRecordBinding
import com.book.readabook.databinding.ActivityTtsBinding
import com.book.readabook.feature.record.RecordViewModel

class TTSActivity : AppCompatActivity() {
    private var binding: ActivityTtsBinding? = null

    private val viewModel: TTSViewModel by lazy {
        ViewModelProvider(this).get(TTSViewModel::class.java)
    }

    private val REQUEST_IMAGE_CAPTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityTtsBinding>(
            this, R.layout.activity_tts
        ).apply {
            lifecycleOwner = this@TTSActivity
            ttsVm = viewModel
        }
        checkPermission()
    }

    fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우
            startProcess()

        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun startProcess() {
        dispatchTakePictureIntent()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            99 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startProcess()
                } else {
                    Log.d("MainActivity", "종료")
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
    }

    // post
    // image 파일로 multipart/form-data

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                var ImnageData: Uri? = data?.data
                makeText(
                    this, ImnageData.toString(),
                    LENGTH_SHORT
                ).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val imageBitmap: Bitmap? = data?.extras?.get("data") as Bitmap
            }
        }
    }
}