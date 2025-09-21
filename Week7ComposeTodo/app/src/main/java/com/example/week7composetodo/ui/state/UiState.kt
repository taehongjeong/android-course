package com.example.week7composetodo.ui.state

/**
 * UI 상태를 나타내는 sealed class - UiState 패턴 구현
 * 
 * UiState 패턴의 장점:
 * 1. 명확한 상태 관리: Loading, Success, Error, Empty 상태를 명시적으로 구분
 * 2. 타입 안전성: sealed class로 모든 상태를 컴파일 타임에 체크
 * 3. 일관성 있는 UI 처리: 모든 화면에서 동일한 패턴으로 상태 관리
 * 4. 테스트 용이성: 각 상태별로 독립적인 테스트 가능
 * 
 * sealed class 사용 이유:
 * - 제한된 상속: 같은 패키지 내에서만 상속 가능
 * - when 표현식에서 모든 케이스 강제: 컴파일러가 누락된 케이스 체크
 * - 타입 안전성: 잘못된 상태 처리 방지
 * 
 * Generic 타입 <out T> 사용:
 * - 공변성(covariance) 지원: UiState<String>을 UiState<Any>로 사용 가능
 * - 다양한 데이터 타입을 동일한 패턴으로 처리
 */
sealed class UiState<out T> {
    
    /**
     * 로딩 상태 - 데이터를 불러오는 중
     * 
     * object 키워드 사용:
     * - 상태를 나타내는 데이터가 필요 없는 경우
     * - 메모리 효율적 (싱글톤 패턴)
     * - 동일한 로딩 상태를 여러 곳에서 재사용
     * 
     * UiState<Nothing> 타입:
     * - 데이터를 포함하지 않는 상태
     * - Nothing은 모든 타입의 하위 타입 (bottom type)
     */
    object Loading : UiState<Nothing>()
    
    /**
     * 성공 상태 - 데이터 로딩 완료
     * 
     * data class 사용:
     * - 실제 데이터를 포함해야 하는 상태
     * - 자동으로 equals(), hashCode(), toString() 생성
     * - copy() 메서드로 불변성 유지하며 데이터 변경 가능
     * 
     * Generic 타입 T:
     * - 다양한 타입의 데이터를 담을 수 있음
     * - List<Todo>, Todo, String 등 모든 타입 지원
     */
    data class Success<T>(val data: T) : UiState<T>()
    
    /**
     * 에러 상태 - 오류 발생
     * 
     * exception: Throwable:
     * - 실제 발생한 예외 객체
     * - 디버깅과 로깅에 활용
     * - 예외 타입별로 다른 처리 가능
     * 
     * message: String? = null:
     * - 사용자에게 표시할 친화적인 메시지
     * - null 가능 타입으로 선택사항
     * - 기본값 null로 예외 메시지 대신 사용 가능
     */
    data class Error(val exception: Throwable, val message: String? = null) : UiState<Nothing>()
    
    /**
     * 빈 상태 - 데이터는 성공적으로 로딩되었으나 결과가 없음
     * 
     * Loading과 구분하는 이유:
     * - Loading: 아직 데이터를 가져오는 중
     * - Empty: 데이터를 가져왔지만 결과가 비어있음
     * - 다른 UI를 보여줘야 함 (로딩 스피너 vs 빈 상태 메시지)
     * 
     * Success와 구분하는 이유:
     * - Success: 실제 데이터가 있는 상태
     * - Empty: 빈 리스트나 null 데이터 상태
     * - 사용자에게 다른 안내 메시지 제공
     */
    object Empty : UiState<Nothing>()
}