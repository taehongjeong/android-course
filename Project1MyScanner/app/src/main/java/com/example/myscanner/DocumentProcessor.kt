package com.example.myscanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ImageFormat
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

object DocumentProcessor {
    
    // ImageProxy를 Bitmap으로 변환
    fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
        return if (image.format == ImageFormat.YUV_420_888) {
            yuv420ToBitmap(image)
        } else {
            null
        }
    }
    
    // YUV_420_888 형식을 Bitmap으로 변환
    private fun yuv420ToBitmap(image: ImageProxy): Bitmap? {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer
        
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        
        val nv21 = ByteArray(ySize + uSize + vSize)
        
        // Y 채널 복사
        yBuffer.get(nv21, 0, ySize)
        
        // U와 V 채널을 인터리빙
        val uvPixelStride = image.planes[1].pixelStride
        if (uvPixelStride == 1) {
            uBuffer.get(nv21, ySize, uSize)
            vBuffer.get(nv21, ySize + uSize, vSize)
        } else {
            var pos = ySize
            for (i in 0 until uSize / uvPixelStride) {
                nv21[pos] = vBuffer.get(i * uvPixelStride)
                nv21[pos + 1] = uBuffer.get(i * uvPixelStride)
                pos += 2
            }
        }
        
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
        val imageBytes = out.toByteArray()
        
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    
    // 그레이스케일 변환
    fun toGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val canvas = Canvas(result)
        val paint = Paint()
        
        val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        
        return result
    }
    
    // 대비 향상 (간단한 히스토그램 스트레칭)
    fun enhanceContrast(bitmap: Bitmap, factor: Float = 1.5f): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        val canvas = Canvas(result)
        val paint = Paint()
        
        val colorMatrix = ColorMatrix().apply {
            setScale(factor, factor, factor, 1f)
            
            // 밝기 조정을 위한 오프셋
            val brightness = -0.5f * factor + 0.5f
            val array = this.array
            array[4] = brightness * 255
            array[9] = brightness * 255
            array[14] = brightness * 255
        }
        
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        
        return result
    }
    
    // 간단한 엣지 검출 (픽셀 단위 처리)
    fun detectEdges(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        
        // 픽셀 배열 가져오기
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        
        val newPixels = IntArray(width * height)
        
        // Sobel 필터 적용 (간단한 버전)
        for (y in 1 until height - 1) {
            for (x in 1 until width - 1) {
                val idx = y * width + x
                
                // 주변 픽셀의 밝기 값 가져오기
                val tl = pixels[(y - 1) * width + (x - 1)] and 0xFF
                val tm = pixels[(y - 1) * width + x] and 0xFF
                val tr = pixels[(y - 1) * width + (x + 1)] and 0xFF
                val ml = pixels[y * width + (x - 1)] and 0xFF
                val mm = pixels[y * width + x] and 0xFF
                val mr = pixels[y * width + (x + 1)] and 0xFF
                val bl = pixels[(y + 1) * width + (x - 1)] and 0xFF
                val bm = pixels[(y + 1) * width + x] and 0xFF
                val br = pixels[(y + 1) * width + (x + 1)] and 0xFF
                
                // 수평 및 수직 그래디언트 계산
                val gx = (tr + 2 * mr + br) - (tl + 2 * ml + bl)
                val gy = (bl + 2 * bm + br) - (tl + 2 * tm + tr)
                
                // 그래디언트 크기 계산
                var magnitude = kotlin.math.sqrt((gx * gx + gy * gy).toDouble()).toInt()
                magnitude = magnitude.coerceIn(0, 255)
                
                // 반전하여 엣지를 검은색으로 표시
                val edgeValue = 255 - magnitude
                
                // 흑백 이미지로 저장
                newPixels[idx] = (0xFF shl 24) or (edgeValue shl 16) or (edgeValue shl 8) or edgeValue
            }
        }
        
        result.setPixels(newPixels, 0, width, 0, 0, width, height)
        return result
    }
    
    // 문서 감지를 위한 윤곽선 강조
    fun enhanceDocumentEdges(bitmap: Bitmap): Bitmap {
        // 1. 그레이스케일 변환
        val grayscale = toGrayscale(bitmap)
        
        // 2. 대비 향상 (더 강하게)
        val enhanced = enhanceContrast(grayscale, 2.2f)
        
        // 3. 엣지 검출
        val edges = detectEdges(enhanced)
        
        return edges
    }
    
    // 문서 스캔용 종합 처리
    fun processForScan(bitmap: Bitmap, detectEdges: Boolean = false): Bitmap {
        return if (detectEdges) {
            // 엣지 검출 모드
            enhanceDocumentEdges(bitmap)
        } else {
            // 일반 스캔 모드
            // 1. 그레이스케일 변환
            val grayscale = toGrayscale(bitmap)
            
            // 2. 대비 향상
            enhanceContrast(grayscale, 1.8f)
        }
    }
    
    // 작은 크기로 리사이징 (성능 최적화)
    fun resizeForProcessing(bitmap: Bitmap, maxSize: Int = 640): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }
        
        val scale = if (width > height) {
            maxSize.toFloat() / width
        } else {
            maxSize.toFloat() / height
        }
        
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}