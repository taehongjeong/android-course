package com.example.week7composetodo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7composetodo.data.Priority
import com.example.week7composetodo.data.Todo
import com.example.week7composetodo.data.TodoRepository
import com.example.week7composetodo.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Todo ViewModel - MVVM 패턴의 핵심 컴포넌트
 * 
 * ViewModel의 역할:
 * 1. UI 상태 관리: UI가 필요한 모든 데이터와 상태를 보관
 * 2. 비즈니스 로직 처리: 데이터 가공, 유효성 검사, 비즈니스 규칙 적용
 * 3. Repository와 UI 사이의 중간 계층 역할
 * 4. 구성 변경(화면 회전 등) 시에도 데이터 유지
 * 
 * @HiltViewModel 어노테이션:
 * - Hilt가 이 ViewModel을 자동으로 생성하고 주입할 수 있게 함
 * - @AndroidEntryPoint가 있는 Activity/Fragment에서 hiltViewModel()로 주입받기 가능
 * - Repository 의존성도 자동으로 주입됨
 * 
 * @Inject constructor:
 * - TodoRepository를 생성자에서 주입받음
 * - Hilt가 Repository 인스턴스를 자동으로 제공
 * - 테스트 시 Mock Repository로 쉽게 대체 가능
 * 
 * ViewModel vs AndroidViewModel:
 * - ViewModel: Application Context가 필요 없는 경우
 * - AndroidViewModel: Application Context가 필요한 경우 (우리는 Repository 패턴을 사용하므로 ViewModel 충분)
 */

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    
    /**
     * ===== UI 상태 관리 영역 =====
     * 
     * StateFlow vs LiveData:
     * - StateFlow: Jetpack Compose에 최적화, 초기값 필수, Flow API 활용 가능
     * - LiveData: View 시스템에 최적화, 라이프사이클 인식
     * 
     * private val _variable + public val variable 패턴:
     * - 캡슐화: 외부에서 직접 상태 변경 불가
     * - 읽기 전용 접근: UI는 상태를 관찰만 가능, 변경은 ViewModel 메서드를 통해서만
     */
    
    /**
     * 전체 Todo 목록의 상태를 관리하는 StateFlow
     * 
     * MutableStateFlow<UiState<List<Todo>>>:
     * - MutableStateFlow: 상태를 변경할 수 있는 Flow
     * - UiState<List<Todo>>: Todo 목록을 UiState 패턴으로 래핑
     * - 초기값 UiState.Loading: 앱 시작 시 로딩 상태로 설정
     * 
     * private 접근자:
     * - ViewModel 내부에서만 상태 변경 가능
     * - 비즈니스 로직의 캡슐화
     */
    private val _todosState = MutableStateFlow<UiState<List<Todo>>>(UiState.Loading)
    
    /**
     * 외부에서 관찰할 수 있는 읽기 전용 StateFlow
     * 
     * asStateFlow():
     * - MutableStateFlow를 읽기 전용 StateFlow로 변환
     * - 외부에서 상태 변경 시도 시 컴파일 에러 발생
     * - Compose에서 collectAsState()로 구독 가능
     */
    val todosState: StateFlow<UiState<List<Todo>>> = _todosState.asStateFlow()
    
    /**
     * 현재 선택된 필터 상태
     * 
     * TodoFilter.ALL을 기본값으로 설정:
     * - 앱 시작 시 모든 Todo를 보여줌
     * - 사용자 친화적인 기본 설정
     */
    private val _filter = MutableStateFlow(TodoFilter.ALL)
    val filter: StateFlow<TodoFilter> = _filter.asStateFlow()
    
    /**
     * 필터가 적용된 Todo 목록 - 복합 상태 관리
     * 
     * combine() 함수의 역할:
     * - 여러 Flow를 결합하여 새로운 Flow 생성
     * - todosState나 filter 중 하나라도 변경되면 자동으로 재계산
     * - 반응형 프로그래밍의 핵심 개념
     * 
     * when 표현식으로 상태별 처리:
     * - UiState.Success인 경우에만 필터링 적용
     * - 다른 상태(Loading, Error, Empty)는 그대로 전달
     * 
     * 필터링 로직:
     * - ALL: 모든 Todo 반환
     * - ACTIVE: 완료되지 않은 Todo만 반환
     * - COMPLETED: 완료된 Todo만 반환
     * 
     * Empty 상태 처리:
     * - 필터링 결과가 비어있으면 UiState.Empty 반환
     * - UI에서 적절한 빈 상태 메시지 표시 가능
     */
    val filteredTodos: StateFlow<UiState<List<Todo>>> = combine(
        todosState,
        filter
    ) { todosState, filter ->
        when (todosState) {
            is UiState.Success -> {
                val filtered = when (filter) {
                    TodoFilter.ALL -> todosState.data
                    TodoFilter.ACTIVE -> todosState.data.filter { !it.isCompleted }
                    TodoFilter.COMPLETED -> todosState.data.filter { it.isCompleted }
                }
                if (filtered.isEmpty()) UiState.Empty else UiState.Success(filtered)
            }
            else -> todosState
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )
    /**
     * stateIn() 함수의 역할:
     * - Flow를 StateFlow로 변환
     * - 여러 구독자가 있을 때 데이터 공유 (Hot Stream)
     * 
     * 매개변수 설명:
     * - scope = viewModelScope: ViewModel 생명주기와 연동
     * - started = SharingStarted.WhileSubscribed(5000): 
     *   구독자가 있을 때만 활성화, 구독자가 없어진 후 5초 후 정지
     * - initialValue = UiState.Loading: 초기값 설정
     */
    
    /**
     * ===== 초기화 영역 =====
     */
    
    /**
     * ViewModel 초기화 블록
     * 
     * init 블록의 역할:
     * - ViewModel 인스턴스 생성 시 자동으로 실행
     * - 초기 데이터 로딩 시작
     * - 생성자에서 할 수 없는 초기화 작업 수행
     * 
     * loadTodos() 호출:
     * - 앱 시작과 동시에 데이터베이스에서 Todo 목록 로딩 시작
     * - Repository의 Flow를 구독하여 실시간 데이터 동기화
     */
    init {
        loadTodos()
    }
    
    /**
     * ===== 데이터 로딩 영역 =====
     */
    
    /**
     * Todo 목록을 Repository에서 불러오는 private 메서드
     * 
     * private 접근자:
     * - ViewModel 내부에서만 사용
     * - UI에서 직접 호출할 필요 없음 (자동으로 실행)
     * 
     * viewModelScope.launch:
     * - ViewModel 전용 코루틴 스코프
     * - ViewModel이 소멸되면 자동으로 코루틴 취소
     * - 메모리 누수 방지
     * 
     * repository.getAllTodos():
     * - Repository에서 Flow<List<Todo>> 반환
     * - 데이터베이스 변경 시 자동으로 새 데이터 방출
     * - Room의 @Query가 반환하는 Flow는 데이터 변경 시 자동 업데이트
     * 
     * .catch { } 연산자:
     * - Flow에서 발생하는 예외 처리
     * - 네트워크 오류, 데이터베이스 오류 등을 안전하게 처리
     * - UiState.Error로 변환하여 UI에 오류 상태 전달
     * 
     * .collect { } 연산자:
     * - Flow에서 방출되는 데이터를 구독
     * - 새 데이터가 방출될 때마다 실행
     * - 데이터가 비어있으면 UiState.Empty, 있으면 UiState.Success로 설정
     */
    private fun loadTodos() {
        viewModelScope.launch {
            repository.getAllTodos()
                .catch { e ->
                    _todosState.value = UiState.Error(e, "할 일 목록을 불러올 수 없습니다.")
                }
                .collect { todos ->
                    _todosState.value = if (todos.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(todos)
                    }
                }
        }
    }
    
    /**
     * ===== UI 이벤트 처리 영역 =====
     * 
     * 다음 메서드들은 UI에서 사용자 상호작용에 따라 호출됩니다.
     * 모든 메서드는 적절한 예외 처리와 에러 상태 관리를 포함합니다.
     */
    
    /**
     * 새로운 Todo 추가
     * 
     * 매개변수:
     * - title: 필수 입력값, 공백 검증 포함
     * - description: 선택적 입력값, 기본값 빈 문자열
     * - priority: 우선순위, 기본값 NORMAL
     * 
     * 유효성 검사:
     * - title.isBlank() 체크로 빈 제목 방지
     * - 조건 실패 시 early return으로 실행 중단
     * 
     * 비즈니스 로직:
     * - UUID 자동 생성으로 고유 ID 보장
     * - 현재 시간 자동 설정 (createdAt)
     * - 기본 완료 상태는 false
     * 
     * 예외 처리:
     * - try-catch로 모든 예외 포착
     * - 사용자 친화적 에러 메시지 제공
     * - UI에서 에러 상태 표시 가능
     */
    fun addTodo(title: String, description: String, priority: Priority = Priority.NORMAL) {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            try {
                val newTodo = Todo(
                    title = title,
                    description = description,
                    priority = priority
                )
                repository.addTodo(newTodo)
            } catch (e: Exception) {
                _todosState.value = UiState.Error(e, "할 일을 추가할 수 없습니다.")
            }
        }
    }
    
    /**
     * 기존 Todo 업데이트
     * 
     * 매개변수:
     * - todo: 수정된 Todo 객체
     * 
     * 동작 방식:
     * - 전체 Todo 객체를 받아서 업데이트
     * - Room의 @Update는 기본키를 기준으로 자동 매칭
     * - 변경된 필드만 실제로 업데이트됨 (Room 최적화)
     * 
     * 사용 사례:
     * - 제목, 설명, 우선순위 수정
     * - 여러 필드를 한 번에 업데이트
     * - copy() 메서드로 불변성 유지하며 수정
     */
    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                repository.updateTodo(todo)
            } catch (e: Exception) {
                _todosState.value = UiState.Error(e, "할 일을 수정할 수 없습니다.")
            }
        }
    }
    
    /**
     * Todo 삭제 (ID 기반)
     * 
     * 매개변수:
     * - todoId: 삭제할 Todo의 고유 ID
     * 
     * ID만 사용하는 이유:
     * - 전체 객체 없이도 삭제 가능
     * - 메모리 효율성 (큰 객체 전달 불필요)
     * - UI에서 ID만 알아도 삭제 가능
     * 
     * 주의사항:
     * - 존재하지 않는 ID로 삭제 시도해도 에러 발생 안 함
     * - Room의 DELETE 쿼리는 매칭되는 행이 없어도 성공 처리
     */
    fun deleteTodo(todoId: String) {
        viewModelScope.launch {
            try {
                repository.deleteTodo(todoId)
            } catch (e: Exception) {
                _todosState.value = UiState.Error(e, "할 일을 삭제할 수 없습니다.")
            }
        }
    }
    
    /**
     * Todo 완료 상태 토글
     * 
     * 매개변수:
     * - todoId: 상태를 변경할 Todo의 ID
     * 
     * 동작 방식:
     * - 현재 완료 상태를 반대로 변경
     * - true → false, false → true
     * - SQL의 NOT 연산자 활용으로 원자적 연산
     * 
     * 사용 사례:
     * - 체크박스 클릭 시
     * - 할 일 완료 처리
     * - 완료된 할 일 다시 활성화
     * 
     * 장점:
     * - 현재 상태를 조회할 필요 없음
     * - 한 번의 데이터베이스 쿼리로 처리
     * - 동시성 문제 방지
     */
    fun toggleComplete(todoId: String) {
        viewModelScope.launch {
            try {
                repository.toggleComplete(todoId)
            } catch (e: Exception) {
                _todosState.value = UiState.Error(e, "상태를 변경할 수 없습니다.")
            }
        }
    }
    
    /**
     * 필터 설정 변경
     * 
     * 매개변수:
     * - newFilter: 새로 적용할 필터 (ALL, ACTIVE, COMPLETED)
     * 
     * 동작 방식:
     * - _filter StateFlow의 값을 직접 변경
     * - combine()으로 연결된 filteredTodos가 자동으로 재계산됨
     * - UI가 자동으로 새로운 필터 결과 표시
     * 
     * 즉시 실행:
     * - suspend 함수가 아님 (데이터베이스 액세스 없음)
     * - 메모리상의 상태만 변경
     * - viewModelScope.launch 불필요
     * 
     * 반응형 업데이트:
     * - 필터 변경 시 UI가 즉시 반응
     * - 기존 데이터를 재사용하여 성능 효율적
     */
    fun setFilter(newFilter: TodoFilter) {
        _filter.value = newFilter
    }
    
    /**
     * ID로 특정 Todo 조회
     * 
     * 매개변수:
     * - todoId: 조회할 Todo의 ID
     * 
     * 반환값:
     * - Todo? : 해당 ID의 Todo 객체 또는 null
     * 
     * suspend 함수인 이유:
     * - Repository의 suspend 함수 호출
     * - 데이터베이스 I/O 작업 포함
     * - 호출 시 코루틴 스코프 내에서 실행 필요
     * 
     * 사용 사례:
     * - 상세 화면에서 기존 Todo 정보 표시
     * - 편집 시 현재 값 로드
     * - 특정 Todo 존재 여부 확인
     * 
     * null 반환 가능:
     * - 해당 ID의 Todo가 존재하지 않는 경우
     * - UI에서 적절한 처리 필요 (에러 메시지, 기본값 등)
     */
    suspend fun getTodoById(todoId: String): Todo? {
        return repository.getTodoById(todoId)
    }
}

/**
 * Todo 필터링 옵션을 나타내는 열거형
 * 
 * enum class 사용 이유:
 * - 타입 안전성: 미리 정의된 값만 사용 가능
 * - 가독성: 코드에서 의미가 명확함
 * - 유지보수: 새로운 필터 추가 시 컴파일러가 누락 체크
 * 
 * 필터 종류:
 * - ALL: 모든 Todo 표시 (기본값)
 * - ACTIVE: 완료되지 않은 Todo만 표시 (isCompleted = false)
 * - COMPLETED: 완료된 Todo만 표시 (isCompleted = true)
 * 
 * UI에서 사용:
 * - 필터 바의 탭/버튼에 연결
 * - ViewModel의 combine() 함수에서 필터링 로직에 활용
 * - 사용자가 선택한 필터에 따라 자동으로 목록 업데이트
 */
enum class TodoFilter {
    ALL, ACTIVE, COMPLETED
}