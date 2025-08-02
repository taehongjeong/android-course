package com.example.week3mystepcounter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

/**
 * Foreground Service for Step Counting using Accelerometer
 */
class StepCounterService : Service(), SensorEventListener {
    
    companion object {
        const val CHANNEL_ID = "StepCounterChannel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_START_SERVICE = "com.example.week3mystepcounter.START_SERVICE"
        const val ACTION_STOP_SERVICE = "com.example.week3mystepcounter.STOP_SERVICE"
        const val ACTION_UPDATE_WIDGET = "com.example.week3mystepcounter.UPDATE_WIDGET"
        
        // 걸음 감지 관련 상수
        private const val STEP_THRESHOLD = 11.0f  // 가속도 임계값
        private const val STEP_DELAY_NS = 250_000_000L  // 250ms (나노초)
        
        // SharedPreferences 관련 상수
        private const val PREFS_NAME = "step_counter_prefs"
        private const val KEY_DAILY_STEPS = "daily_steps"
        private const val KEY_CURRENT_STEPS = "current_steps"
        private const val KEY_LAST_UPDATE_DATE = "last_update_date"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        
        fun getTodayDate(): String {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return sdf.format(Date())
        }
    }
    
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var stepCount = 0
    private var lastAcceleration = 0f
    private var lastStepTime = 0L
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate() {
        super.onCreate()
        
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        // 센서 매니저 초기화
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        // 알림 채널 생성
        createNotificationChannel()
        
        // 날짜가 바뀌었는지 확인하고 초기화
        checkAndResetIfNewDay()
        
        // 오늘 걸음 수 로드
        loadTodaySteps()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                startForegroundService()
                startStepCounting()
                // 서비스 실행 상태 저장
                sharedPreferences.edit {
                    putBoolean("service_running", true)
                }
            }
            ACTION_STOP_SERVICE -> {
                stopStepCounting()
                // 서비스 중지 상태 저장
                sharedPreferences.edit {
                    putBoolean("service_running", false)
                }
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        stopStepCounting()
        // 서비스 중지 상태 저장
        sharedPreferences.edit {
            putBoolean("service_running", false)
        }
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.service_notification_title),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.widget_description)
            setShowBadge(false)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
    
    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_text, stepCount))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }
    
    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun startStepCounting() {
        accelerometer?.let { sensor ->
            sensorManager.registerListener(
                this, 
                sensor, 
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }
    
    private fun stopStepCounting() {
        sensorManager.unregisterListener(this)
    }
    
    private fun checkAndResetIfNewDay() {
        val today = getTodayDate()
        val lastUpdateDate = sharedPreferences.getString(KEY_LAST_UPDATE_DATE, "")
        
        if (lastUpdateDate != today) {
            // 새로운 날이므로 오늘 걸음 수 초기화
            sharedPreferences.edit().apply {
                putInt(KEY_CURRENT_STEPS, 0)
                putString(KEY_LAST_UPDATE_DATE, today)
                
                // 어제 걸음 수를 일별 기록에 저장
                if (lastUpdateDate!!.isNotEmpty()) {
                    saveDailyStepRecord(lastUpdateDate, sharedPreferences.getInt(KEY_CURRENT_STEPS, 0))
                }
                
                apply()
            }
        }
    }
    
    private fun saveDailyStepRecord(date: String, stepCount: Int) {
        try {
            val dailyStepsJson = sharedPreferences.getString(KEY_DAILY_STEPS, "{}")
            val dailySteps = JSONObject(dailyStepsJson ?: "{}")
            dailySteps.put(date, stepCount)
            
            sharedPreferences.edit {
                putString(KEY_DAILY_STEPS, dailySteps.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun loadTodaySteps() {
        stepCount = sharedPreferences.getInt(KEY_CURRENT_STEPS, 0)
        updateNotification()
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            if (sensorEvent.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = sensorEvent.values[0]
                val y = sensorEvent.values[1]
                val z = sensorEvent.values[2]
                
                // 가속도 크기 계산
                val acceleration = sqrt(x * x + y * y + z * z)
                val deltaAcceleration = acceleration - lastAcceleration
                lastAcceleration = acceleration
                
                // 걸음 감지 로직
                if (deltaAcceleration > STEP_THRESHOLD) {
                    val currentTime = System.nanoTime()
                    if (currentTime - lastStepTime > STEP_DELAY_NS) {
                        lastStepTime = currentTime
                        onStepDetected()
                    }
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 정확도 변경 시 처리할 내용
    }
    
    private fun onStepDetected() {
        stepCount++
        
        // SharedPreferences 업데이트
        sharedPreferences.edit {
            putInt(KEY_CURRENT_STEPS, stepCount)
        }
        
        // UI 업데이트
        updateNotification()
        
        // 위젯 업데이트
        val widgetIntent = Intent(this, StepCounterWidgetProvider::class.java)
        widgetIntent.action = ACTION_UPDATE_WIDGET
        sendBroadcast(widgetIntent)
    }
}