package com.example.week7composetodo.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.week7composetodo.data.Priority
import com.example.week7composetodo.data.Todo
import com.example.week7composetodo.ui.component.PrioritySelector
import com.example.week7composetodo.ui.component.TodoInputField
import com.example.week7composetodo.viewmodel.TodoViewModel

/**
 * Todo 상세/편집 화면 - 추가와 수정을 모두 처리하는 통합 화면
 * 
 * 화면 모드:
 * 1. 추가 모드: todoId가 null인 경우
 * 2. 편집 모드: todoId가 있는 경우
 * 
 * 주요 기능:
 * - 제목, 설명, 우선순위 입력/수정
 * - 실시간 유효성 검사
 * - 키보드 최적화 (IME Action)
 * - 상태 기반 UI 업데이트
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    /**
     * todoId: String? - 편집할 Todo의 ID (null이면 새 Todo 추가)
     * viewModel: TodoViewModel - ViewModel 주입 (기본값: hiltViewModel)
     * onNavigateBack: () -> Unit - 뒤로가기 콜백
     */
    todoId: String?,
    viewModel: TodoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    /**
     * ===== 상태 관리 영역 =====
     * 
     * 로컬 상태:
     * - 폼 입력값들을 로컬에서 관리
     * - 사용자 입력 즉시 반영
     * - 저장 시점에만 ViewModel로 전달
     */
    var existingTodo by remember { mutableStateOf<Todo?>(null) }
    
    /**
     * LaunchedEffect - 부수 효과(Side Effect) 처리
     * 
     * 용도:
     * - todoId 변경 시 기존 Todo 데이터 로딩
     * - suspend 함수 호출 (getTodoById)
     * - 컴포지션 생명주기와 연동
     */
    LaunchedEffect(todoId) {
        if (todoId != null) {
            existingTodo = viewModel.getTodoById(todoId)
        }
    }
    
    /**
     * 폼 입력 상태들 - remember로 리컴포지션 간 유지
     * 
     * 초기값 설정:
     * - 편집 모드: 기존 값 사용
     * - 추가 모드: 기본값 사용
     */
    var title by remember { mutableStateOf(existingTodo?.title ?: "") }
    var description by remember { mutableStateOf(existingTodo?.description ?: "") }
    var priority by remember { mutableStateOf(existingTodo?.priority ?: Priority.NORMAL) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(if (existingTodo != null) "할 일 수정" else "새 할 일")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 제목 입력
            TodoInputField(
                value = title,
                onValueChange = { title = it },
                label = "제목",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                isError = title.isBlank() && existingTodo == null,
                errorMessage = if (title.isBlank() && existingTodo == null) "제목을 입력해주세요" else null
            )
            
            // 설명 입력
            TodoInputField(
                value = description,
                onValueChange = { description = it },
                label = "설명 (선택)",
                singleLine = false,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )
            
            // 우선순위 선택
            PrioritySelector(
                selectedPriority = priority,
                onPrioritySelected = { priority = it }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            /**
             * 저장 버튼 - 추가/수정 로직 통합 처리
             * 
             * 비즈니스 로직:
             * 1. 제목 유효성 검사 (공백 불허)
             * 2. 모드별 분기 (추가 vs 수정)
             * 3. ViewModel 메서드 호출
             * 4. 성공 시 이전 화면으로 복귀
             * 
             * 수정 모드 처리:
             * - copy() 메서드로 불변성 유지
             * - 필요한 필드만 변경
             * 
             * enabled 상태:
             * - 제목이 비어있지 않을 때만 활성화
             * - 사용자 피드백으로 UX 향상
             */
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val currentTodo = existingTodo
                        if (currentTodo != null) {
                            // 수정 모드: 기존 Todo 업데이트
                            viewModel.updateTodo(
                                currentTodo.copy(
                                    title = title,
                                    description = description,
                                    priority = priority
                                )
                            )
                        } else {
                            // 추가 모드: 새 Todo 생성
                            viewModel.addTodo(title, description, priority)
                        }
                        onNavigateBack() // 저장 후 이전 화면으로
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank()
            ) {
                Text(if (existingTodo != null) "수정" else "추가")
            }
        }
    }
}