package com.example.myscanner

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class DocumentAnalyzer(
    private val onImageProcessed: (Bitmap?) -> Unit,
    private val isEdgeDetectionEnabled: () -> Boolean = { false }
) : ImageAnalysis.Analyzer {
    
    companion object {
        private const val TAG = "DocumentAnalyzer"
        private const val PROCESS_INTERVAL = 15 // 15프레임마다 처리
    }
    
    private var frameCount = 0
    
    override fun analyze(image: ImageProxy) {
        frameCount++
        
        // 15프레임마다 이미지 처리
        if (frameCount % PROCESS_INTERVAL == 0) {
            try {
                // ImageProxy를 Bitmap으로 변환
                val bitmap = DocumentProcessor.imageProxyToBitmap(image)
                
                if (bitmap != null) {
                    // 성능을 위해 이미지 크기 축소
                    val resized = DocumentProcessor.resizeForProcessing(bitmap, 480)
                    
                    // 문서 스캔 처리 적용 (엣지 검출 모드 지원)
                    val processed = DocumentProcessor.processForScan(resized, isEdgeDetectionEnabled())
                    
                    // UI 업데이트를 위해 콜백 호출
                    onImageProcessed(processed)
                    
                    val modeText = if (isEdgeDetectionEnabled()) "edge detection" else "normal scan"
                    Log.d(TAG, "Frame $frameCount processed ($modeText), size: ${processed.width}x${processed.height}")
                    
                    // 원본 비트맵 메모리 해제
                    if (bitmap != resized) {
                        bitmap.recycle()
                    }
                } else {
                    Log.w(TAG, "Failed to convert ImageProxy to Bitmap")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing image", e)
            }
        }
        
        // 이미지 처리 완료 후 반드시 close 호출
        image.close()
    }
}