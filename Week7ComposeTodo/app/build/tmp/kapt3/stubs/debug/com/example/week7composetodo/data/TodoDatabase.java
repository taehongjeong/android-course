package com.example.week7composetodo.data;

/**
 * Todo 앱의 Room 데이터베이스 정의
 *
 * Room Database 아키텍처:
 * 1. Database 클래스: 데이터베이스 설정과 DAO 접근점
 * 2. Entity 클래스: 테이블 구조 정의
 * 3. DAO 인터페이스: 데이터베이스 연산 메서드
 *
 * @Database 어노테이션 매개변수:
 * - entities: 데이터베이스에 포함될 엔티티 목록
 * - version: 데이터베이스 스키마 버전 (마이그레이션 시 사용)
 * - exportSchema: 스키마 내보내기 여부 (false로 설정하여 빌드 간단화)
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/week7composetodo/data/TodoDatabase;", "Landroidx/room/RoomDatabase;", "<init>", "()V", "todoDao", "Lcom/example/week7composetodo/data/TodoDao;", "app_debug"})
@androidx.room.Database(entities = {com.example.week7composetodo.data.Todo.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.example.week7composetodo.data.Converters.class})
public abstract class TodoDatabase extends androidx.room.RoomDatabase {
    
    public TodoDatabase() {
        super();
    }
    
    /**
     * TodoDao 인스턴스를 반환하는 추상 메서드
     *
     * abstract 사용 이유:
     * - Room이 컴파일 시에 실제 구현체를 자동 생성
     * - 개발자는 메서드 시그니처만 정의하고 구현은 Room에 위임
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.week7composetodo.data.TodoDao todoDao();
}