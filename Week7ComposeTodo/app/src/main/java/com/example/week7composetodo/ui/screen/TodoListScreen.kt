package com.example.week7composetodo.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.week7composetodo.ui.component.*
import com.example.week7composetodo.ui.state.UiState
import com.example.week7composetodo.viewmodel.TodoFilter
import com.example.week7composetodo.viewmodel.TodoViewModel

/**
 * Todo 목록 화면 - Jetpack Compose UI 구현
 * 
 * 화면의 역할:
 * 1. Todo 목록 표시 및 관리
 * 2. 필터링 기능 (전체/진행중/완료)
 * 3. Todo 추가, 수정, 삭제, 완료 처리
 * 4. 빈 상태, 로딩, 에러 상태 처리
 * 
 * Compose UI 패턴:
 * - Stateless Composable: 상태를 직접 관리하지 않고 ViewModel에서 받아옴
 * - Single Source of Truth: ViewModel이 유일한 데이터 소스
 * - Unidirectional Data Flow: 데이터는 위에서 아래로, 이벤트는 아래에서 위로
 * 
 * Material Design 3 적용:
 * - 최신 Material Design 가이드라인 준수
 * - 동적 컬러, 개선된 접근성, 적응형 레이아웃
 */

/**
 * @OptIn(ExperimentalMaterial3Api::class):
 * - Material3의 실험적 API 사용을 명시적으로 허용
 * - TopAppBar 등 일부 컴포넌트가 아직 실험적 단계
 * - 향후 API 변경 가능성을 인지하고 사용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    /**
     * 매개변수 설명:
     * 
     * viewModel: TodoViewModel = hiltViewModel():
     * - 기본값으로 Hilt에서 주입받은 ViewModel 사용
     * - 테스트나 미리보기에서는 다른 ViewModel 주입 가능
     * - 의존성 주입의 유연성 제공
     * 
     * onNavigateToDetail: (String?) -> Unit:
     * - 상세 화면으로 이동하는 콜백 함수
     * - String?: Todo ID (null이면 새 Todo 추가)
     * - Navigation 로직을 상위 컴포넌트에 위임
     */
    viewModel: TodoViewModel = hiltViewModel(),
    onNavigateToDetail: (String?) -> Unit
) {
    /**
     * ===== 상태 관찰 영역 =====
     * 
     * collectAsState():
     * - StateFlow를 Compose State로 변환
     * - StateFlow 값이 변경되면 자동으로 리컴포지션 트리거
     * - Compose 생명주기에 맞춰 자동으로 구독/해제
     * 
     * by 위임 프로퍼티:
     * - State<T>.value 접근을 T로 간소화
     * - todosState.value 대신 todosState로 직접 접근
     */
    val todosState by viewModel.filteredTodos.collectAsState()
    val filter by viewModel.filter.collectAsState()
    
    /**
     * Todo 개수 계산 - 파생 상태(Derived State)
     * 
     * remember(todosState):
     * - todosState가 변경될 때만 재계산
     * - 불필요한 계산 방지로 성능 최적화
     * - 메모이제이션 패턴
     * 
     * 파생 상태 패턴:
     * - 기존 상태에서 계산된 새로운 상태
     * - 원본 상태 변경 시 자동으로 업데이트
     * - UI에 표시할 통계 정보 생성
     */
    val todoCount = remember(todosState) {
        when (val state = todosState) {
            is UiState.Success -> TodoCount(
                total = state.data.size,
                active = state.data.count { !it.isCompleted },
                completed = state.data.count { it.isCompleted }
            )
            else -> TodoCount(0, 0, 0)
        }
    }
    
    /**
     * ===== UI 구조 정의 영역 =====
     * 
     * Scaffold - Material Design 기본 레이아웃 구조
     * 
     * Scaffold의 역할:
     * - 화면의 기본 구조 제공 (TopBar, BottomBar, FAB, Content 등)
     * - Material Design 가이드라인 준수
     * - 자동 패딩 계산 및 충돌 방지
     * - 일관된 사용자 경험 제공
     */
    Scaffold(
        /**
         * topBar - 상단 앱바 정의
         * 
         * TopAppBar:
         * - 화면 제목과 네비게이션 아이콘 표시
         * - Material3의 개선된 디자인 적용
         * - 스크롤 시 자동 숨김/표시 기능 (필요시)
         */
        topBar = {
            TopAppBar(
                title = { Text("My Todo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        /**
         * floatingActionButton - 주요 액션 버튼
         * 
         * FAB 사용 이유:
         * - 가장 중요한 액션 (새 Todo 추가) 강조
         * - 접근성 향상 (큰 터치 영역)
         * - Material Design 권장 패턴
         * 
         * onClick = { onNavigateToDetail(null) }:
         * - null 전달로 새 Todo 추가 모드 활성화
         * - 콜백 함수로 네비게이션 로직 위임
         */
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToDetail(null) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { paddingValues ->
        /**
         * ===== 메인 컨텐츠 영역 =====
         * 
         * Column 레이아웃:
         * - 수직으로 컴포넌트 배치
         * - 필터바 + 컨텐츠 영역 구성
         * 
         * paddingValues:
         * - Scaffold에서 계산된 패딩값 적용
         * - TopBar, FAB와의 겹침 방지
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            /**
             * 필터 바 - 조건부 애니메이션 표시
             * 
             * AnimatedVisibility:
             * - 컴포넌트의 나타남/사라짐 애니메이션
             * - 로딩 중이 아닐 때만 표시
             * - 부드러운 사용자 경험 제공
             */
            AnimatedVisibility(
                visible = todosState !is UiState.Loading,
                enter = fadeIn() + slideInVertically()
            ) {
                TodoFilterBar(
                    selectedFilter = filter,
                    onFilterSelected = viewModel::setFilter,
                    todoCount = todoCount
                )
            }
            
            /**
             * Todo 목록 - UiState 패턴으로 상태별 UI 처리
             * 
             * when 표현식의 장점:
             * - 모든 상태를 명시적으로 처리
             * - 컴파일러가 누락된 케이스 체크
             * - 타입 안전성 보장
             */
            when (val state = todosState) {
                /**
                 * 로딩 상태 - 중앙 정렬 로딩 인디케이터
                 */
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                /**
                 * 빈 상태 - 필터별 맞춤 메시지
                 * 
                 * 필터별 메시지 분기:
                 * - 사용자 상황에 맞는 안내 제공
                 * - 다음 액션 유도
                 */
                is UiState.Empty -> {
                    EmptyStateView(
                        title = when (filter) {
                            TodoFilter.ALL -> "할 일이 없습니다"
                            TodoFilter.ACTIVE -> "진행중인 할 일이 없습니다"
                            TodoFilter.COMPLETED -> "완료된 할 일이 없습니다"
                        },
                        message = when (filter) {
                            TodoFilter.ALL -> "새로운 할 일을 추가해보세요!"
                            TodoFilter.ACTIVE -> "새로운 할 일을 추가하거나 완료된 항목을 확인해보세요"
                            TodoFilter.COMPLETED -> "아직 완료된 항목이 없습니다"
                        }
                    )
                }
                
                /**
                 * 성공 상태 - LazyColumn으로 효율적인 목록 표시
                 * 
                 * LazyColumn 성능 최적화:
                 * - 화면에 보이는 항목만 컴포즈
                 * - 스크롤 시 동적으로 생성/제거
                 * - 대용량 리스트에 적합
                 * 
                 * items() key 매개변수:
                 * - 각 항목의 고유 식별자 지정
                 * - 리컴포지션 시 효율적인 업데이트
                 * - 애니메이션 성능 향상
                 */
                is UiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.data,
                            key = { it.id }  // 성능 최적화를 위한 키 지정
                        ) { todo ->
                            /**
                             * 개별 Todo 항목 애니메이션
                             * 
                             * AnimatedVisibility:
                             * - 항목 추가/제거 시 부드러운 애니메이션
                             * - spring 애니메이션으로 자연스러운 움직임
                             */
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(animationSpec = spring()),
                                exit = fadeOut() + shrinkVertically(animationSpec = spring())
                            ) {
                                TodoItem(
                                    todo = todo,
                                    onToggleComplete = { viewModel.toggleComplete(todo.id) },
                                    onDelete = { viewModel.deleteTodo(todo.id) },
                                    onClick = { onNavigateToDetail(todo.id) }
                                )
                            }
                        }
                    }
                }
                
                /**
                 * 에러 상태 - 사용자 친화적 에러 처리
                 * 
                 * 에러 UI 구성:
                 * - 명확한 에러 메시지
                 * - 재시도 버튼 제공
                 * - 중앙 정렬로 주목도 향상
                 */
                is UiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message ?: "오류가 발생했습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: 재시도 로직 구현 */ }
                        ) {
                            Text("다시 시도")
                        }
                    }
                }
            }
        }
    }
}