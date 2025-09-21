package com.example.week7composetodo;

/**
 * Todo 앱의 메인 Application 클래스
 *
 * 주요 역할:
 * 1. Hilt 의존성 주입을 위한 진입점 역할
 * 2. 앱 전체의 생명주기 관리
 * 3. 전역 설정 및 초기화 담당
 *
 * @HiltAndroidApp 어노테이션:
 * - Hilt의 코드 생성을 트리거하는 어노테이션
 * - Application 레벨에서 Hilt 컨테이너를 생성
 * - 앱 전체에서 의존성 주입을 사용할 수 있게 해줌
 *
 * Hilt DI (Dependency Injection) 장점:
 * - 객체 생성과 관리를 자동화
 * - 테스트 시 모킹 용이
 * - 코드의 결합도 감소, 유지보수성 향상
 */
@dagger.hilt.android.HiltAndroidApp()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/example/week7composetodo/TodoApplication;", "Landroid/app/Application;", "<init>", "()V", "app_debug"})
public final class TodoApplication extends android.app.Application {
    
    public TodoApplication() {
        super();
    }
}