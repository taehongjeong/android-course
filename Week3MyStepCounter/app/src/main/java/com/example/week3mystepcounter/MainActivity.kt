package com.example.week3mystepcounter

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.edit

/**
 * Main Activity for Step Counter App
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var tvStepCount: MaterialTextView
    private lateinit var tvDate: MaterialTextView
    private lateinit var tvGoal: MaterialTextView
    private lateinit var btnToggleService: MaterialButton
    private lateinit var btnResetSteps: MaterialButton
    
    private lateinit var sharedPreferences: SharedPreferences
    private var isServiceRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null
    
    companion object {
        private const val PREFS_NAME = "step_counter_prefs"
        private const val KEY_DAILY_STEPS = "daily_steps"
        private const val KEY_CURRENT_STEPS = "current_steps"
        private const val KEY_LAST_UPDATE_DATE = "last_update_date"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        
        private fun getTodayDate(): String {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return sdf.format(Date())
        }
        
        private fun getFormattedDate(dateString: String): String {
            return try {
                val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                val date = sdf.parse(dateString)
                val displayFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
                displayFormat.format(date ?: Date())
            } catch (_: Exception) {
                dateString
            }
        }
    }
    
    // 권한 요청 launcher
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val activityRecognitionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions[Manifest.permission.ACTIVITY_RECOGNITION] ?: false
        } else {
            true // API 29 미만에서는 권한 불필요
        }
        val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
        } else {
            true
        }
        
        if (activityRecognitionGranted && notificationGranted) {
            setupStepCounter()
        } else {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        
        // 날짜가 바뀌었는지 확인하고 초기화
        checkAndResetIfNewDay()
        
        // 권한 확인 및 요청
        checkAndRequestPermissions()
    }
    
    override fun onResume() {
        super.onResume()
        updateStepDisplay()
        checkServiceStatus()
        startPeriodicUpdate()
    }
    
    override fun onPause() {
        super.onPause()
        stopPeriodicUpdate()
    }
    
    private fun initViews() {
        tvStepCount = findViewById(R.id.tv_step_count)
        tvDate = findViewById(R.id.tv_date)
        tvGoal = findViewById(R.id.tv_goal)
        btnToggleService = findViewById(R.id.btn_toggle_service)
        btnResetSteps = findViewById(R.id.btn_reset_steps)
        
        btnToggleService.setOnClickListener {
            toggleStepCountingService()
        }
        
        btnResetSteps.setOnClickListener {
            resetTodaySteps()
        }
        
        // 날짜 표시
        tvDate.text = getFormattedDate(getTodayDate())
    }
    
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        
        // ACTIVITY_RECOGNITION 권한 확인 (API 29+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
            }
        }
        
        // POST_NOTIFICATIONS 권한 확인 (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) 
                != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        
        // SCHEDULE_EXACT_ALARM 권한 확인 (API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // 설정 화면으로 이동하여 권한 요청
                Toast.makeText(this, getString(R.string.permission_exact_alarm), Toast.LENGTH_LONG).show()
                val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            setupStepCounter()
        }
    }
    
    private fun setupStepCounter() {
        // 초기 걸음 수 표시
        updateStepDisplay()
        
        // 자정 알람 설정
        MidnightResetReceiver.scheduleNextAlarm(this)
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
    
    private fun startPeriodicUpdate() {
        updateRunnable = object : Runnable {
            override fun run() {
                updateStepDisplay()
                handler.postDelayed(this, 1000) // 1초마다 업데이트
            }
        }
        handler.post(updateRunnable!!)
    }
    
    private fun stopPeriodicUpdate() {
        updateRunnable?.let { handler.removeCallbacks(it) }
    }
    
    private fun updateStepDisplay() {
        val stepCount = sharedPreferences.getInt(KEY_CURRENT_STEPS, 0)
        tvStepCount.text = stepCount.toString()
        updateGoalProgress(stepCount)
    }
    
    private fun updateGoalProgress(stepCount: Int) {
        val goal = 10000  // 목표: 10,000걸음
        val percentage = (stepCount * 100) / goal
        tvGoal.text = getString(R.string.goal_format, stepCount, goal, percentage)
    }
    
    private fun checkServiceStatus() {
        // SharedPreferences에서 서비스 상태 확인
        isServiceRunning = sharedPreferences.getBoolean("service_running", false)
        updateServiceButton()
    }
    
    private fun updateServiceButton() {
        if (isServiceRunning) {
            btnToggleService.text = getString(R.string.btn_stop_service)
            btnToggleService.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
        } else {
            btnToggleService.text = getString(R.string.btn_start_service)
            btnToggleService.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
        }
    }
    
    private fun toggleStepCountingService() {
        val intent = Intent(this, StepCounterService::class.java)
        
        if (isServiceRunning) {
            // 서비스 중지
            intent.action = StepCounterService.ACTION_STOP_SERVICE
            startService(intent)
            isServiceRunning = false
            sharedPreferences.edit {
                putBoolean("service_running", false)
            }
            Toast.makeText(this, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show()
        } else {
            // 서비스 시작
            intent.action = StepCounterService.ACTION_START_SERVICE
            startForegroundService(intent)
            isServiceRunning = true
            sharedPreferences.edit {
                putBoolean("service_running", true)
            }
            Toast.makeText(this, getString(R.string.service_started), Toast.LENGTH_SHORT).show()
        }
        
        updateServiceButton()
        
        // 위젯 업데이트
        StepCounterWidgetProvider.updateAllWidgets(this)
    }
    
    private fun resetTodaySteps() {
        sharedPreferences.edit {
            putInt(KEY_CURRENT_STEPS, 0)
        }
            
        Toast.makeText(this, getString(R.string.steps_reset), Toast.LENGTH_SHORT).show()
        updateStepDisplay()
        
        // 위젯 업데이트
        StepCounterWidgetProvider.updateAllWidgets(this)
    }
    
}