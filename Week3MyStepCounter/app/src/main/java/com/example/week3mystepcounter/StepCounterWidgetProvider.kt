package com.example.week3mystepcounter

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * App Widget Provider for Step Counter
 */
class StepCounterWidgetProvider : AppWidgetProvider() {
    
    companion object {

        // SharedPreferences 관련 상수
        private const val PREFS_NAME = "step_counter_prefs"
        private const val KEY_CURRENT_STEPS = "current_steps"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        
        fun getTodayDate(): String {
            val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            return sdf.format(Date())
        }
        
        fun getFormattedDate(dateString: String): String {
            return try {
                val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                val date = sdf.parse(dateString)
                val displayFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
                displayFormat.format(date ?: Date())
            } catch (e: Exception) {
                dateString
            }
        }
        
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            stepCount: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.step_counter_widget)
            
            // 걸음 수 업데이트
            views.setTextViewText(R.id.tv_step_count, stepCount.toString())
            views.setTextViewText(R.id.tv_step_label, "걸음")
            views.setTextViewText(R.id.tv_date, getFormattedDate(getTodayDate()))
            
            // 앱 실행 인텐트
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
            
            // 위젯 업데이트
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, StepCounterWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            
            if (appWidgetIds.isNotEmpty()) {
                val intent = Intent(context, StepCounterWidgetProvider::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                context.sendBroadcast(intent)
            }
        }
    }
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // 모든 위젯 업데이트
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        
        context ?: return
        
        if (intent?.action == StepCounterService.ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, StepCounterWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
    }
    
    private fun updateWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        try {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val stepCount = sharedPreferences.getInt(KEY_CURRENT_STEPS, 0)
            
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, stepCount)
            }
        } catch (e: Exception) {
            // 에러 발생 시 0으로 표시
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, 0)
            }
        }
    }

}