package com.example.week7composetodo.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.week7composetodo.data.Priority
import com.example.week7composetodo.data.Todo

/**
 * TodoItem - 재사용 가능한 Todo 항목 컴포넌트
 * 
 * 재사용 가능한 Composable의 특징:
 * 1. 단일 책임: 하나의 Todo 항목만 표시
 * 2. 상태 없음: 모든 데이터를 매개변수로 받음
 * 3. 이벤트 위임: 클릭, 삭제 등 이벤트를 콜백으로 전달
 * 4. 커스터마이징 가능: Modifier를 통한 외관 조정
 * 
 * Material Design 적용:
 * - SwipeToDismiss 제스처
 * - 애니메이션 효과
 * - Material3 컬러 시스템
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showDeleteDialog = true
                false // Don't dismiss immediately, wait for dialog confirmation
            } else {
                false
            }
        }
    )
    
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    else -> Color.Transparent
                },
                label = "background_color"
            )
            
            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                label = "icon_scale"
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier.scale(scale),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        content = {
    
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onClick,
                colors = CardDefaults.cardColors(
                    containerColor = if (todo.isCompleted) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 체크박스
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = when (todo.priority) {
                        Priority.QUICK -> MaterialTheme.colorScheme.error
                        Priority.HIGH -> MaterialTheme.colorScheme.error
                        Priority.NORMAL -> MaterialTheme.colorScheme.primary
                        Priority.LOW -> MaterialTheme.colorScheme.secondary
                    }
                )
            )
            
            // Todo 내용
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                // 제목
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (todo.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // 설명 (있는 경우)
                if (todo.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // 우선순위 표시
                Spacer(modifier = Modifier.height(4.dp))
                PriorityChip(priority = todo.priority)
            }
            
                }
            }
        }
    )
    
    // 삭제 확인 다이얼로그
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            todoTitle = todo.title,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
    
    // Reset swipe state when dialog is dismissed
    LaunchedEffect(showDeleteDialog) {
        if (!showDeleteDialog) {
            dismissState.reset()
        }
    }
}

@Composable
fun PriorityChip(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val color = when (priority) {
        Priority.QUICK -> MaterialTheme.colorScheme.error
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.NORMAL -> MaterialTheme.colorScheme.primary
        Priority.LOW -> MaterialTheme.colorScheme.secondary
    }
    
    val text = when (priority) {
        Priority.QUICK -> "당장"
        Priority.HIGH -> "높음"
        Priority.NORMAL -> "보통"
        Priority.LOW -> "낮음"
    }
    
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.15f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    todoTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "할 일 삭제")
        },
        text = {
            Text(text = "'$todoTitle'을(를) 삭제하시겠습니까?")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("삭제")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}