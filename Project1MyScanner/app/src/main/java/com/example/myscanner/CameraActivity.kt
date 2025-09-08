package com.example.myscanner

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.example.myscanner.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var isScanModeEnabled = false
    private var isEdgeDetectionEnabled = false
    
    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HHmmss"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // 전체 화면 설정 (setContentView 이후에 호출)
        setupFullScreen()
        
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        setupUI()
        startCamera()
    }
    
    private fun setupFullScreen() {
        // 상태바와 네비게이션 바 숨기기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(android.view.WindowInsets.Type.statusBars() or android.view.WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        }
        
        // 화면 켜짐 유지
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
    
    private fun setupUI() {
        // 촬영 버튼
        binding.btnCapture.setOnClickListener {
            takePhoto()
        }
        
        // 촬영 버튼 - 길게 누르면 엣지 검출 모드 토글
        binding.btnCapture.setOnLongClickListener {
            toggleEdgeDetection()
            true
        }
        
        // 뒤로가기 버튼
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        // 갤러리 버튼 - 길게 누르면 스캔 모드 토글
        binding.btnGallery.setOnClickListener {
            openGallery()
        }
        
        binding.btnGallery.setOnLongClickListener {
            toggleScanMode()
            true
        }
    }
    
    private fun toggleScanMode() {
        isScanModeEnabled = !isScanModeEnabled
        
        if (isScanModeEnabled) {
            binding.processedImageView.visibility = View.VISIBLE
            val modeText = if (isEdgeDetectionEnabled) "스캔 모드 활성화 (엣지 검출)" else "스캔 모드 활성화"
            Toast.makeText(this, modeText, Toast.LENGTH_SHORT).show()
        } else {
            binding.processedImageView.visibility = View.GONE
            binding.processedImageView.setImageBitmap(null)
            Toast.makeText(this, "스캔 모드 비활성화", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun toggleEdgeDetection() {
        isEdgeDetectionEnabled = !isEdgeDetectionEnabled
        
        val message = if (isEdgeDetectionEnabled) {
            "엣지 검출 모드 활성화"
        } else {
            "일반 스캔 모드"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        
        // 스캔 모드가 활성화되어 있지 않으면 자동으로 활성화
        if (!isScanModeEnabled && isEdgeDetectionEnabled) {
            toggleScanMode()
        }
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            
            // Preview 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.previewView.surfaceProvider
                }
            
            // ImageCapture 설정 - 회전 보정 추가
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .build()
            
            // ImageAnalysis 설정
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetRotation(windowManager.defaultDisplay.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, DocumentAnalyzer(
                        onImageProcessed = { processedBitmap ->
                            // UI 스레드에서 이미지 업데이트
                            runOnUiThread {
                                if (isScanModeEnabled && processedBitmap != null) {
                                    binding.processedImageView.setImageBitmap(processedBitmap)
                                }
                            }
                        },
                        isEdgeDetectionEnabled = { isEdgeDetectionEnabled }
                    ))
                }
            
            // 카메라 선택 (후면 카메라)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            try {
                // 기존 바인딩 해제
                cameraProvider.unbindAll()
                
                // 카메라 바인딩 - imageAnalyzer 추가
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
                
            } catch (exc: Exception) {
                Log.e(TAG, "카메라 바인딩 실패", exc)
                Toast.makeText(
                    this,
                    "카메라를 시작할 수 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        
        // 파일명 생성
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())
        
        // 앱 내부 저장소에 저장
        val photosDir = File(filesDir, "photos")
        if (!photosDir.exists()) {
            photosDir.mkdirs()
        }
        
        val photoFile = File(photosDir, "$name.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        // 사진 촬영
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri ?: android.net.Uri.fromFile(photoFile)
                    
                    // 이미지 회전 보정
                    correctImageOrientation(photoFile)
                    
                    // MediaStore에도 저장 (갤러리에 표시되도록)
                    saveToMediaStore(photoFile)
                    
                    Toast.makeText(
                        this@CameraActivity,
                        "사진이 저장되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // 결과 반환
                    val resultIntent = Intent().apply {
                        putExtra("photo_path", photoFile.absolutePath)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "사진 저장 실패: ${exception.message}", exception)
                    Toast.makeText(
                        this@CameraActivity,
                        "사진 저장에 실패했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
    
    private fun saveToMediaStore(photoFile: File) {
        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, photoFile.name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyScanner")
                }
            }
            
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    photoFile.inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "MediaStore 저장 실패", e)
        }
    }
    
    private fun correctImageOrientation(photoFile: File) {
        try {
            val exif = ExifInterface(photoFile.absolutePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipBitmap(bitmap, horizontal = true)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipBitmap(bitmap, horizontal = false)
                else -> bitmap
            }
            
            // 회전된 이미지를 다시 저장
            if (rotatedBitmap != bitmap) {
                FileOutputStream(photoFile).use { out ->
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }
                rotatedBitmap.recycle()
            }
            bitmap.recycle()
            
        } catch (e: Exception) {
            Log.e(TAG, "이미지 회전 보정 실패", e)
        }
    }
    
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    private fun flipBitmap(bitmap: Bitmap, horizontal: Boolean): Bitmap {
        val matrix = Matrix().apply {
            if (horizontal) {
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            } else {
                postScale(1f, -1f, bitmap.width / 2f, bitmap.height / 2f)
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    
    private fun openGallery() {
        val photosDir = File(filesDir, "photos")
        if (!photosDir.exists() || photosDir.listFiles()?.isEmpty() == true) {
            Toast.makeText(
                this,
                "저장된 사진이 없습니다.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        
        // 갤러리 앱 열기
        val intent = Intent(Intent.ACTION_VIEW).apply {
            type = "image/*"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}