package com.example.week7composetodo.di;

/**
 * Hilt 데이터베이스 모듈 - 의존성 주입 설정
 *
 * Hilt Module의 역할:
 * 1. 의존성 주입을 위한 객체 생성 방법 정의
 * 2. 생명주기 관리와 스코프 제어
 * 3. 인터페이스와 구현체 바인딩
 * 4. 싱글톤 패턴 자동 관리
 *
 * @Module 어노테이션:
 * - Hilt에게 이 클래스가 의존성 주입 모듈임을 알림
 * - @Provides 메서드들을 포함하여 객체 생성 방법 제공
 *
 * @InstallIn(SingletonComponent::class):
 * - 이 모듈을 SingletonComponent에 설치
 * - Application 생명주기와 동일하게 관리
 * - 앱 전체에서 동일한 인스턴스 공유
 *
 * object 키워드 사용:
 * - 코틀린의 싱글톤 객체
 * - 인스턴스가 하나만 존재함을 보장
 * - 메소드만 제공하고 상태를 갖지 않음
 */
@dagger.Module()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0007H\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0007\u00a8\u0006\u000b"}, d2 = {"Lcom/example/week7composetodo/di/DatabaseModule;", "", "<init>", "()V", "provideTodoDatabase", "Lcom/example/week7composetodo/data/TodoDatabase;", "context", "Landroid/content/Context;", "provideTodoDao", "Lcom/example/week7composetodo/data/TodoDao;", "database", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
@kotlin.Suppress(names = {"unused"})
public final class DatabaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.example.week7composetodo.di.DatabaseModule INSTANCE = null;
    
    private DatabaseModule() {
        super();
    }
    
    /**
     * TodoDatabase 인스턴스 제공 메서드
     *
     * @Provides 어노테이션:
     * - Hilt에게 이 메서드가 의존성을 제공함을 알림
     * - TodoDatabase 타입이 필요할 때 이 메서드 호출
     *
     * @Singleton 어노테이션:
     * - 앱 전체에서 하나의 인스턴스만 생성
     * - 데이터베이스 연결 비용 절약과 데이터 일관성 보장
     *
     * @ApplicationContext 어노테이션:
     * - Application Context를 주입받음
     * - Activity Context와 달리 메모리 누수 방지
     * - 데이터베이스는 Application 생명주기와 동일해야 안전
     */
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.TodoDatabase provideTodoDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    /**
     * TodoDao 인스턴스 제공 메서드
     *
     * 의존성 체인:
     * - TodoDao를 요청하면 Hilt가 자동으로 TodoDatabase를 먼저 생성
     * - TodoDatabase에서 todoDao() 메서드를 호출하여 DAO 인스턴스 반환
     *
     * @Singleton 어노테이션:
     * - DAO도 싱글톤으로 관리하여 데이터 일관성 보장
     * - 동일한 Database 인스턴스에서 생성된 DAO 공유
     *
     * 매개변수 database: TodoDatabase:
     * - Hilt가 자동으로 provideTodoDatabase() 결과를 주입
     * - 의존성 그래프에서 자동 해결
     */
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.TodoDao provideTodoDao(@org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.TodoDatabase database) {
        return null;
    }
}