package com.example.week7composetodo;

/**
 * Todo 앱의 메인 Activity 클래스
 *
 * 현대적 Android 개발 패턴:
 * 1. 단일 Activity 아키텍처 (Single Activity Architecture)
 * 2. Jetpack Compose를 사용한 선언형 UI
 * 3. Hilt를 통한 의존성 주입
 *
 * @AndroidEntryPoint 어노테이션:
 * - Hilt가 이 클래스에 의존성을 주입할 수 있도록 함
 * - Activity 레벨에서 ViewModel 등을 자동으로 주입받을 수 있음
 * - Hilt 컴포넌트 트리에서 Activity 컴포넌트를 생성
 */
@dagger.hilt.android.AndroidEntryPoint()
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0014\u00a8\u0006\b"}, d2 = {"Lcom/example/week7composetodo/MainActivity;", "Landroidx/activity/ComponentActivity;", "<init>", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class MainActivity extends androidx.activity.ComponentActivity {
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
}