package com.example.week7composetodo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Todo Repository 클래스 - Repository 패턴 구현
 * 
 * Repository 패턴의 장점:
 * 1. 데이터 소스 추상화: DAO, 네트워크, 캐시 등을 통합 관리
 * 2. 비즈니스 로직 분리: ViewModel에서 데이터 소스 세부사항 숨김
 * 3. 테스트 용이성: Mock Repository로 대체 가능
 * 4. 데이터 일관성: 단일 진실 공급원(Single Source of Truth)
 * 
 * @Singleton 어노테이션:
 * - 앵 전체에서 하나의 인스턴스만 생성
 * - 메모리 비용 절약과 데이터 일관성 보장
 * - 여러 ViewModel에서 동일한 Repository 인스턴스 공유
 * 
 * @Inject constructor:
 * - Hilt의 의존성 주입 사용
 * - TodoDao를 자동으로 주입받음
 * - 수동 객체 생성 및 관리 불필요
 */
@Singleton
class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    
    /**
     * 모든 Todo 목록 조회
     * 
     * 직접 DAO 메서드 위임:
     * - Repository는 데이터 소스 조정자 역할
     * - 향후 네트워크, 캐시 등 추가 시 이 메서드에서 통합 처리
     * - Flow를 그대로 반환하여 반응형 프로그래밍 지원
     */
    fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()
    
    /**
     * ID로 특정 Todo 조회
     * 
     * suspend 함수 유지:
     * - DAO의 suspend 함수를 호출하므로 Repository도 suspend로 정의
     * - 비동기 처리 체인 유지
     */
    suspend fun getTodoById(todoId: String): Todo? = todoDao.getTodoById(todoId)
    
    /**
     * 새로운 Todo 추가
     * 
     * 매개변수로 Todo 객체 받음:
     * - ViewModel에서 생성한 객체를 그대로 전달
     * - Repository에서는 데이터 검증이나 변환 등 추가 로직 처리 가능
     */
    suspend fun addTodo(todo: Todo) {
        todoDao.insertTodo(todo)
    }
    
    /**
     * 기존 Todo 업데이트
     * 
     * 전체 객체 업데이트:
     * - Room의 @Update는 기본키를 기준으로 자동 매칭
     * - 변경된 필드만 업데이트되어 효율적
     */
    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }
    
    /**
     * Todo 삭제 (ID로)
     * 
     * ID만으로 삭제:
     * - 전체 객체 없이도 삭제 가능
     * - 네트워크 비용이나 메모리 사용량 절약
     */
    suspend fun deleteTodo(todoId: String) {
        todoDao.deleteTodoById(todoId)
    }
    
    /**
     * Todo 완료 상태 토글
     * 
     * 비즈니스 로직 위임:
     * - DAO의 최적화된 쿼리 활용
     * - 원자적 연산으로 데이터 일관성 보장
     */
    suspend fun toggleComplete(todoId: String) {
        todoDao.toggleComplete(todoId)
    }
}