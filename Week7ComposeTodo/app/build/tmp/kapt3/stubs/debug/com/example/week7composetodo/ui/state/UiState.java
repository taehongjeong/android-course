package com.example.week7composetodo.ui.state;

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
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002:\u0004\u0005\u0006\u0007\bB\t\b\u0004\u00a2\u0006\u0004\b\u0003\u0010\u0004\u0082\u0001\u0004\t\n\u000b\f\u00a8\u0006\r"}, d2 = {"Lcom/example/week7composetodo/ui/state/UiState;", "T", "", "<init>", "()V", "Loading", "Success", "Error", "Empty", "Lcom/example/week7composetodo/ui/state/UiState$Empty;", "Lcom/example/week7composetodo/ui/state/UiState$Error;", "Lcom/example/week7composetodo/ui/state/UiState$Loading;", "Lcom/example/week7composetodo/ui/state/UiState$Success;", "app_debug"})
public abstract class UiState<T extends java.lang.Object> {
    
    private UiState() {
        super();
    }
    
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
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0003\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/example/week7composetodo/ui/state/UiState$Empty;", "Lcom/example/week7composetodo/ui/state/UiState;", "", "<init>", "()V", "app_debug"})
    public static final class Empty extends com.example.week7composetodo.ui.state.UiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.week7composetodo.ui.state.UiState.Empty INSTANCE = null;
        
        private Empty() {
        }
    }
    
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
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0004\b\u0007\u0010\bJ\t\u0010\r\u001a\u00020\u0004H\u00c6\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u001f\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0017"}, d2 = {"Lcom/example/week7composetodo/ui/state/UiState$Error;", "Lcom/example/week7composetodo/ui/state/UiState;", "", "exception", "", "message", "", "<init>", "(Ljava/lang/Throwable;Ljava/lang/String;)V", "getException", "()Ljava/lang/Throwable;", "getMessage", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Error extends com.example.week7composetodo.ui.state.UiState {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.Throwable exception = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String message = null;
        
        public Error(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable exception, @org.jetbrains.annotations.Nullable()
        java.lang.String message) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Throwable getException() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getMessage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.Throwable component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.week7composetodo.ui.state.UiState.Error copy(@org.jetbrains.annotations.NotNull()
        java.lang.Throwable exception, @org.jetbrains.annotations.Nullable()
        java.lang.String message) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
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
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0003\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/example/week7composetodo/ui/state/UiState$Loading;", "Lcom/example/week7composetodo/ui/state/UiState;", "", "<init>", "()V", "app_debug"})
    public static final class Loading extends com.example.week7composetodo.ui.state.UiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.week7composetodo.ui.state.UiState.Loading INSTANCE = null;
        
        private Loading() {
        }
    }
    
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
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u000f\u0012\u0006\u0010\u0003\u001a\u00028\u0001\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\t\u001a\u00028\u0001H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u0001H\u00c6\u0001\u00a2\u0006\u0002\u0010\u000bJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u00d6\u0003J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001R\u0013\u0010\u0003\u001a\u00028\u0001\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0014"}, d2 = {"Lcom/example/week7composetodo/ui/state/UiState$Success;", "T", "Lcom/example/week7composetodo/ui/state/UiState;", "data", "<init>", "(Ljava/lang/Object;)V", "getData", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "copy", "(Ljava/lang/Object;)Lcom/example/week7composetodo/ui/state/UiState$Success;", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Success<T extends java.lang.Object> extends com.example.week7composetodo.ui.state.UiState<T> {
        private final T data = null;
        
        public Success(T data) {
        }
        
        public final T getData() {
            return null;
        }
        
        public final T component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.week7composetodo.ui.state.UiState.Success<T> copy(T data) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}