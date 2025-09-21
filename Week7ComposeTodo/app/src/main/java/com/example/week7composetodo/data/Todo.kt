package com.example.week7composetodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Todo 엔티티 클래스 - Room Database의 테이블 정의
 * 
 * Room ORM (Object-Relational Mapping) 패턴:
 * 1. 데이터베이스 테이블을 Kotlin 객체로 매핑
 * 2. 타입 안전성과 컴파일 타임 검증 제공
 * 3. SQLite 쿼리를 간편하게 작성 가능
 * 
 * @Entity 어노테이션:
 * - 이 클래스가 Room 데이터베이스의 테이블임을 명시
 * - tableName: 실제 SQLite 테이블 이름 지정
 * 
 * data class 사용 이유:
 * - 자동으로 equals(), hashCode(), toString() 등 메서드 생성
 * - 불변성(immutability) 보장으로 데이터 안전성 향상
 * - copy() 메서드로 일부 필드만 변경한 새 객체 생성 가능
 */
@Entity(tableName = "todos")
data class Todo(
    /**
     * 기본키 필드
     * @PrimaryKey: Room에서 이 필드를 기본키로 인식
     * UUID 사용 이유:
     * - 전역적으로 고유한 식별자 보장
     * - 여러 기기 간 동기화 시 충돌 방지
     * - 보안상 예측 불가능한 ID
     */
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    
    /**
     * Todo 제목 (필수 필드)
     * 사용자가 입력하는 주요 내용
     */
    val title: String,
    
    /**
     * Todo 상세 설명 (선택 필드)
     * 기본값을 빈 문자열로 설정하여 선택사항으로 처리
     */
    val description: String = "",
    
    /**
     * 완료 상태 플래그
     * 기본값 false로 설정하여 새로 생성된 Todo는 미완료 상태
     */
    val isCompleted: Boolean = false,
    
    /**
     * 생성 시간 (타임스탬프)
     * System.currentTimeMillis()로 현재 시간을 밀리초 단위로 저장
     * 정렬이나 필터링에 활용
     */
    val createdAt: Long = System.currentTimeMillis(),
    
    /**
     * 우선순위 설정
     * enum을 사용하여 타입 안전성 보장
     * 기본값은 NORMAL 우선순위
     */
    val priority: Priority = Priority.NORMAL
)

/**
 * Todo 우선순위를 나타내는 열거형
 * 
 * enum class 사용 장점:
 * 1. 타입 안전성: 정의된 값만 사용 가능
 * 2. 가독성: 코드에서 의미가 명확함
 * 3. 유지보수: 새로운 우선순위 추가 시 컴파일러가 누락 체크
 * 
 * 우선순위 단계:
 * - LOW: 낮은 우선순위 (여유가 있을 때 처리)
 * - NORMAL: 보통 우선순위 (기본값)
 * - HIGH: 높은 우선순위 (우선적으로 처리)
 */
enum class Priority {
    LOW, NORMAL, HIGH, QUICK
}