package com.example.week7composetodo.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * EmptyStateView - 빈 상태를 위한 공통 컴포넌트
 * 
 * 공통 컴포넌트의 장점:
 * 1. 일관성: 앱 전체에서 동일한 빈 상태 UI
 * 2. 재사용성: 다양한 상황에서 활용 가능
 * 3. 유지보수: 한 곳에서 스타일 관리
 * 4. 접근성: 표준화된 접근성 지원
 * 
 * UX 고려사항:
 * - 명확한 상황 설명
 * - 다음 액션 유도
 * - 친근한 톤앤매너
 */

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier,
    title: String = "할 일이 없습니다",
    message: String = "새로운 할 일을 추가해보세요!",
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // 이모지 또는 아이콘 영역
            Text(
                text = "📝",
                style = MaterialTheme.typography.displayLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 제목
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 메시지
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            // 액션 버튼 (옵션)
            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(actionLabel)
                }
            }
        }
    }
}

@Composable
@Suppress("unused")
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String = "불러오는 중..."
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Suppress("unused")
fun ErrorView(
    modifier: Modifier = Modifier,
    message: String = "오류가 발생했습니다",
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // 에러 아이콘
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 에러 메시지
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
            
            // 재시도 버튼
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedButton(
                    onClick = onRetry,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("다시 시도")
                }
            }
        }
    }
}