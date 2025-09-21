package com.example.week7composetodo.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.week7composetodo.viewmodel.TodoFilter

/**
 * TodoFilterBar - 필터링과 통계를 담당하는 컴포넌트
 * 
 * 컴포넌트 설계 원칙:
 * 1. 관심사 분리: 필터링 UI만 담당
 * 2. 데이터 기반: 외부에서 받은 데이터로만 UI 구성
 * 3. 상태 호이스팅: 필터 상태를 상위에서 관리
 * 4. 애니메이션 통합: 부드러운 사용자 경험
 * 
 * Material3 Filter Chip 활용:
 * - 선택 상태 시각화
 * - 개수 배지 표시
 * - 접근성 지원
 */

@Composable
fun TodoFilterBar(
    modifier: Modifier = Modifier,
    selectedFilter: TodoFilter,
    onFilterSelected: (TodoFilter) -> Unit,
    todoCount: TodoCount
) {
    Column(modifier = modifier) {
        // 필터 칩들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TodoFilter.entries.forEach { filter ->
                TodoFilterChip(
                    filter = filter,
                    selected = selectedFilter == filter,
                    count = when (filter) {
                        TodoFilter.ALL -> todoCount.total
                        TodoFilter.ACTIVE -> todoCount.active
                        TodoFilter.COMPLETED -> todoCount.completed
                    },
                    onClick = { onFilterSelected(filter) }
                )
            }
        }
        
        // 통계 정보
        AnimatedVisibility(
            visible = todoCount.total > 0,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            TodoStatistics(todoCount = todoCount)
        }
    }
}

@Composable
private fun TodoFilterChip(
    filter: TodoFilter,
    selected: Boolean,
    count: Int,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = when (filter) {
                        TodoFilter.ALL -> "전체"
                        TodoFilter.ACTIVE -> "진행중"
                        TodoFilter.COMPLETED -> "완료"
                    }
                )
                
                // 개수 표시
                if (count > 0) {
                    Badge(
                        containerColor = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    ) {
                        Text(
                            text = count.toString(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun TodoStatistics(
    todoCount: TodoCount,
    modifier: Modifier = Modifier
) {
    val targetCompletionRate = if (todoCount.total > 0) {
        (todoCount.completed.toFloat() / todoCount.total * 100).toInt()
    } else {
        0
    }
    
    val completionRate by animateIntAsState(
        targetValue = targetCompletionRate,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "completion_rate"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticItem(
                label = "전체",
                value = todoCount.total.toString()
            )
            
            StatisticItem(
                label = "진행중",
                value = todoCount.active.toString(),
                color = MaterialTheme.colorScheme.primary
            )
            
            StatisticItem(
                label = "완료",
                value = todoCount.completed.toString(),
                color = MaterialTheme.colorScheme.tertiary
            )
            
            StatisticItem(
                label = "완료율",
                value = "$completionRate%",
                color = if (completionRate >= 70) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun StatisticItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

data class TodoCount(
    val total: Int = 0,
    val active: Int = 0,
    val completed: Int = 0
)