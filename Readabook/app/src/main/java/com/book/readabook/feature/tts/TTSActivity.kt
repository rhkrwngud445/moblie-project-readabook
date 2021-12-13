package com.book.readabook.feature.tts

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.book.readabook.R
import com.book.readabook.databinding.ActivityRecordBinding
import com.book.readabook.databinding.ActivityTtsBinding
import com.book.readabook.feature.record.RecordViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import android.graphics.BitmapFactory
import android.os.Build
import android.speech.tts.TextToSpeech
import androidx.core.net.toUri
import com.book.readabook.global.Application
import com.book.readabook.model.data.RecordData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


// 카메라 찍는 화면을 mainActivity에서 하는건 어떨까? - 카메라intent에서 뒤로가기 버튼을 누를때의 처리 고민

class TTSActivity : AppCompatActivity() {
    lateinit var currentPhotoPath: String
    private var binding: ActivityTtsBinding? = null
    var text : String? = null

    private val viewModel by lazy {
        ViewModelProvider(this, TTSViewModel.Factory(application as Application)).get(TTSViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityTtsBinding>(
            this, R.layout.activity_tts
        ).apply {
            lifecycleOwner = this@TTSActivity
            ttsVm = viewModel
        }
        checkPermission()
        initObserve()
        initClickListener()
        initTextToSpeech()
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
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.book.readabook.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, 2)
                }
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
                createMultipartFile()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun createMultipartFile() {
        val file = File(currentPhotoPath)
        var inputStream: InputStream? = null
        try {
            inputStream = baseContext.getContentResolver().openInputStream(file.toUri())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val byteArrayOutputStream = ByteArrayOutputStream()
        val nh = (bitmap.getHeight() * (512.0 / bitmap.getWidth()))
        val scaled = Bitmap.createScaledBitmap(bitmap, 512, nh.toInt(), true)
        scaled.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val requestFile = RequestBody.create(
            "image/jpeg".toMediaTypeOrNull(),
            byteArrayOutputStream.toByteArray()
        )
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        viewModel.getTextOcr("KakaoAK " + getString(R.string.kakao_key), body)

    }

    private fun initObserve(){
        viewModel.get_response.observe(this, androidx.lifecycle.Observer {
            if(it.result!!.size!=0){
                for(i in 0..it.result!!.size-1){
                    text+= it.result[i]!!.recognition_words!!.get(0)
                    text+="\n"
                }
            }
            binding!!.tvOcr.text= text
            if(text!= null) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insert(RecordData(text!!))
                }
            }
        })
    }

    private var tts: TextToSpeech? = null

    private fun initTextToSpeech() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {   // 롤리팝(api level: 21, android 5.0) 이상에서 지원
            Toast.makeText(this, "SDK version is low", Toast.LENGTH_SHORT).show()
            return
        }

        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this, "TTS setting successed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "TTS init failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ttsSpeak(strTTS: String) {
        tts?.speak(strTTS, TextToSpeech.QUEUE_ADD, null, null)
    }

    private fun initClickListener(){
        binding!!.btTts.setOnClickListener {
            if(text!=null){
                ttsSpeak(text!!)
            }
            else{
                makeText(this,"text is empty!",Toast.LENGTH_SHORT)
            }
        }
    }

    // 1. tts button
    // 2. database for record(date, content)
    // https://m.blog.naver.com/yuyyulee/221531478175

}