package com.example.week7composetodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.week7composetodo.navigation.TodoNavigation
import com.example.week7composetodo.ui.theme.Week7ComposeTodoTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Todo 앱의 메인 Activity 클래스
 * 
 * 현대적 Android 개발 패턴:
 * 1. 단일 Activity 아키텍처 (Single Activity Architecture)
 * 2. Jetpack Compose를 사용한 선언형 UI
 * 3. Hilt를 통한 의존성 주입
 * 
 * @AndroidEntryPoint 어노테이션:
 * - Hilt가 이 클래스에 의존성을 주입할 수 있도록 함
 * - Activity 레벨에서 ViewModel 등을 자동으로 주입받을 수 있음
 * - Hilt 컴포넌트 트리에서 Activity 컴포넌트를 생성
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge(): 전체 화면을 사용하여 몰입형 경험 제공
        enableEdgeToEdge()
        
        // setContent: Compose UI를 설정하는 함수
        // 기존 XML 레이아웃 대신 Composable 함수로 UI 구성
        setContent {
            // Week7ComposeTodoTheme: Material Design 3 테마 적용
            Week7ComposeTodoTheme {
                // Surface: Material Design의 기본 표면 컴포넌트
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TodoNavigation: 네비게이션 그래프 시작점
                    // Navigation Compose를 사용한 화면 전환 관리
                    TodoNavigation()
                }
            }
        }
    }
}