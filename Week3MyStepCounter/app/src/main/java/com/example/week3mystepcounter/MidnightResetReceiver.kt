package com.example.week3mystepcounter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.edit

/**
 * BroadcastReceiver for midnight step count reset
 */
class MidnightResetReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "MidnightResetReceiver"
        const val ACTION_MIDNIGHT_RESET = "com.example.week3mystepcounter.MIDNIGHT_RESET"
        
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
        
        fun scheduleNextAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MidnightResetReceiver::class.java).apply {
                action = ACTION_MIDNIGHT_RESET
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // 다음날 자정 시간 계산
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            // 정확한 알람 설정 (API 레벨에 따라 다른 메서드 사용)
            try {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                        // API 31+ : canScheduleExactAlarms() 확인 필요
                        if (alarmManager.canScheduleExactAlarms()) {
                            alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                pendingIntent
                            )
                            Log.d(TAG, "다음 자정 알람 설정 (API 31+): ${calendar.time}")
                        } else {
                            // 정확한 알람 권한이 없으면 일반 알람 사용
                            alarmManager.setAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                pendingIntent
                            )
                            Log.d(TAG, "다음 자정 알람 설정 (일반): ${calendar.time}")
                        }
                    }
                    else -> {
                        // API 23+ : setExactAndAllowWhileIdle 사용
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                        Log.d(TAG, "다음 자정 알람 설정 (API 23+): ${calendar.time}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "알람 설정 실패: ${e.message}")
            }
        }
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        
        when (intent?.action) {
            ACTION_MIDNIGHT_RESET -> {
                Log.d(TAG, "자정 리셋 실행")
                performMidnightReset(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d(TAG, "부팅 완료 - 알람 재설정")
                scheduleNextAlarm(context)
            }
            "android.intent.action.TIME_SET",
            "android.intent.action.TIMEZONE_CHANGED" -> {
                Log.d(TAG, "시간/시간대 변경 - 알람 재설정")
                scheduleNextAlarm(context)
            }
        }
    }
    
    private fun performMidnightReset(context: Context) {
        try {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val today = getTodayDate()
            val currentSteps = sharedPreferences.getInt(KEY_CURRENT_STEPS, 0)
            val lastUpdateDate = sharedPreferences.getString(KEY_LAST_UPDATE_DATE, "")
            
            // 어제 걸음 수를 일별 기록에 저장
            if (!lastUpdateDate.isNullOrEmpty() && lastUpdateDate != today) {
                saveDailyStepRecord(sharedPreferences, lastUpdateDate, currentSteps)
            }
            
            // 오늘 걸음 수 초기화
            sharedPreferences.edit().apply {
                putInt(KEY_CURRENT_STEPS, 0)
                putString(KEY_LAST_UPDATE_DATE, today)
                apply()
            }
            
            Log.d(TAG, "자정 리셋 완료: 새로운 날짜 $today 로 초기화")
            
            // 위젯 업데이트
            val widgetIntent = Intent(context, StepCounterWidgetProvider::class.java)
            widgetIntent.action = StepCounterService.ACTION_UPDATE_WIDGET
            context.sendBroadcast(widgetIntent)
            
            // 다음 알람 설정
            scheduleNextAlarm(context)
            
        } catch (e: Exception) {
            Log.e(TAG, "자정 리셋 실패: ${e.message}")
        }
    }
    
    private fun saveDailyStepRecord(sharedPreferences: SharedPreferences, date: String, stepCount: Int) {
        try {
            val dailyStepsJson = sharedPreferences.getString(KEY_DAILY_STEPS, "{}")
            val dailySteps = JSONObject(dailyStepsJson ?: "{}")
            dailySteps.put(date, stepCount)
            
            sharedPreferences.edit {
                putString(KEY_DAILY_STEPS, dailySteps.toString())
            }
                
            Log.d(TAG, "일별 기록 저장: $date = $stepCount 걸음")
        } catch (e: Exception) {
            Log.e(TAG, "일별 기록 저장 실패: ${e.message}")
        }
    }
}