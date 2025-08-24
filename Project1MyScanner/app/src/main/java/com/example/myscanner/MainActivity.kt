package com.example.myscanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    // 카메라 액티비티 결과 처리
    private val cameraActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }
    
    // 권한 요청 처리
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            openCameraActivity()
        } else {
            Toast.makeText(
                this,
                "카메라와 저장소 권한이 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }
    
    private fun setupUI() {
        // FAB 클릭 리스너
        binding.fabCamera.setOnClickListener {
            checkPermissionsAndOpenCamera()
        }
    }
    
    private fun checkPermissionsAndOpenCamera() {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA
        )
        
        // Android 10 미만에서만 저장소 권한 필요
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        
        val allPermissionsGranted = permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == 
                PackageManager.PERMISSION_GRANTED
        }
        
        if (allPermissionsGranted) {
            openCameraActivity()
        } else {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }
    
    private fun openCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        cameraActivityLauncher.launch(intent)
    }
}