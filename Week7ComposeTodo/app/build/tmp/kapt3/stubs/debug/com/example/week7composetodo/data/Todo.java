package com.example.week7composetodo.data;

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
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0004\b\f\u0010\rJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u000bH\u00c6\u0003JE\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u00072\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0012R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006#"}, d2 = {"Lcom/example/week7composetodo/data/Todo;", "", "id", "", "title", "description", "isCompleted", "", "createdAt", "", "priority", "Lcom/example/week7composetodo/data/Priority;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZJLcom/example/week7composetodo/data/Priority;)V", "getId", "()Ljava/lang/String;", "getTitle", "getDescription", "()Z", "getCreatedAt", "()J", "getPriority", "()Lcom/example/week7composetodo/data/Priority;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "todos")
public final class Todo {
    
    /**
     * 기본키 필드
     * @PrimaryKey: Room에서 이 필드를 기본키로 인식
     * UUID 사용 이유:
     * - 전역적으로 고유한 식별자 보장
     * - 여러 기기 간 동기화 시 충돌 방지
     * - 보안상 예측 불가능한 ID
     */
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    
    /**
     * Todo 제목 (필수 필드)
     * 사용자가 입력하는 주요 내용
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    
    /**
     * Todo 상세 설명 (선택 필드)
     * 기본값을 빈 문자열로 설정하여 선택사항으로 처리
     */
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String description = null;
    
    /**
     * 완료 상태 플래그
     * 기본값 false로 설정하여 새로 생성된 Todo는 미완료 상태
     */
    private final boolean isCompleted = false;
    
    /**
     * 생성 시간 (타임스탬프)
     * System.currentTimeMillis()로 현재 시간을 밀리초 단위로 저장
     * 정렬이나 필터링에 활용
     */
    private final long createdAt = 0L;
    
    /**
     * 우선순위 설정
     * enum을 사용하여 타입 안전성 보장
     * 기본값은 NORMAL 우선순위
     */
    @org.jetbrains.annotations.NotNull()
    private final com.example.week7composetodo.data.Priority priority = null;
    
    public Todo(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean isCompleted, long createdAt, @org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Priority priority) {
        super();
    }
    
    /**
     * 기본키 필드
     * @PrimaryKey: Room에서 이 필드를 기본키로 인식
     * UUID 사용 이유:
     * - 전역적으로 고유한 식별자 보장
     * - 여러 기기 간 동기화 시 충돌 방지
     * - 보안상 예측 불가능한 ID
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    /**
     * Todo 제목 (필수 필드)
     * 사용자가 입력하는 주요 내용
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    /**
     * Todo 상세 설명 (선택 필드)
     * 기본값을 빈 문자열로 설정하여 선택사항으로 처리
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    /**
     * 완료 상태 플래그
     * 기본값 false로 설정하여 새로 생성된 Todo는 미완료 상태
     */
    public final boolean isCompleted() {
        return false;
    }
    
    /**
     * 생성 시간 (타임스탬프)
     * System.currentTimeMillis()로 현재 시간을 밀리초 단위로 저장
     * 정렬이나 필터링에 활용
     */
    public final long getCreatedAt() {
        return 0L;
    }
    
    /**
     * 우선순위 설정
     * enum을 사용하여 타입 안전성 보장
     * 기본값은 NORMAL 우선순위
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.Priority getPriority() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.Priority component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.Todo copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, boolean isCompleted, long createdAt, @org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Priority priority) {
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