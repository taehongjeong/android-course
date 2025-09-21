package com.example.week7composetodo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Todo DAO (Data Access Object) - 데이터베이스 접근 메서드 정의
 * 
 * DAO 패턴의 장점:
 * 1. 데이터베이스 연산의 추상화
 * 2. SQL 쿼리의 타입 안전성 보장
 * 3. 컴파일 타임에 SQL 문법 검증
 * 4. 코루틴과 자연스럽게 연동
 * 
 * @Dao 어노테이션:
 * - Room에게 이 인터페이스가 DAO임을 알림
 * - 컴파일 시 Room이 구현체를 자동 생성
 * - interface로 정의하여 구현체는 Room에 위임
 */
@Dao
@Suppress("unused")
interface TodoDao {
    
    /**
     * 모든 Todo 목록을 생성일 순으로 조회
     * 
     * Flow<List<Todo>> 사용 이유:
     * - 데이터베이스 변경 시 자동으로 UI 업데이트
     * - 반응형 프로그래밍 지원
     * - 코루틴과 함께 비동기 처리 가능
     * 
     * @Query 어노테이션:
     * - 직접 SQL 쿼리 작성
     * - ORDER BY createdAt DESC: 최근 생성된 순서로 정렬
     */
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<Todo>>
    
    /**
     * ID로 특정 Todo 조회
     * 
     * suspend 함수 사용 이유:
     * - 메인 스레드 블록킹 방지
     * - 코루틴 스코프 내에서만 호출 가능
     * - 비동기 처리로 성능 향상
     * 
     * 반환타입 Todo?:
     * - null 가능한 타입으로 ID에 매칭되는 데이터가 없을 수 있음
     */
    @Query("SELECT * FROM todos WHERE id = :todoId")
    suspend fun getTodoById(todoId: String): Todo?
    
    /**
     * 새로운 Todo 삽입
     * 
     * @Insert 어노테이션:
     * - Room이 자동으로 INSERT SQL 생성
     * - onConflict = OnConflictStrategy.REPLACE: 동일 ID 존재 시 덮어쓰기
     * 
     * OnConflictStrategy 옵션:
     * - REPLACE: 기존 데이터 덮어쓰기
     * - IGNORE: 충돌 시 무시
     * - ABORT: 충돌 시 오류 발생
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)
    
    /**
     * 기존 Todo 업데이트
     * 
     * @Update 어노테이션:
     * - 기본키를 기준으로 자동으로 UPDATE SQL 생성
     * - 변경된 필드만 업데이트 (효율적)
     */
    @Update
    suspend fun updateTodo(todo: Todo)
    
    /**
     * Todo 객체로 삭제
     * 
     * @Delete 어노테이션:
     * - 기본키를 기준으로 자동으로 DELETE SQL 생성
     * - 전체 객체를 매개변수로 받음
     */
    @Delete
    suspend fun deleteTodo(todo: Todo)
    
    /**
     * ID로 Todo 삭제
     * 
     * 사용 상황:
     * - Todo 객체 없이 ID만 알고 있을 때
     * - 네트워크나 메모리 비용 절약
     */
    @Query("DELETE FROM todos WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: String)
    
    /**
     * Todo 완료 상태 토글
     * 
     * SQL의 NOT 연산자 활용:
     * - isCompleted가 true면 false로, false면 true로 변경
     * - 한 번의 쿼리로 상태 변경 가능
     * - 원자적 연산 보장
     */
    @Query("UPDATE todos SET isCompleted = NOT isCompleted WHERE id = :todoId")
    suspend fun toggleComplete(todoId: String)
}