package com.example.week7composetodo.ui.component;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aD\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\u0010\b\u0002\u0010\b\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\tH\u0007\u001a\u001c\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u0007\u001a.\u0010\u000b\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u0010\b\u0002\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0001\u0018\u00010\tH\u0007\u00a8\u0006\r"}, d2 = {"EmptyStateView", "", "modifier", "Landroidx/compose/ui/Modifier;", "title", "", "message", "actionLabel", "onAction", "Lkotlin/Function0;", "LoadingView", "ErrorView", "onRetry", "app_debug"})
public final class EmptyStateViewKt {
    
    /**
     * EmptyStateView - 빈 상태를 위한 공통 컴포넌트
     *
     * 공통 컴포넌트의 장점:
     * 1. 일관성: 앱 전체에서 동일한 빈 상태 UI
     * 2. 재사용성: 다양한 상황에서 활용 가능
     * 3. 유지보수: 한 곳에서 스타일 관리
     * 4. 접근성: 표준화된 접근성 지원
     *
     * UX 고려사항:
     * - 명확한 상황 설명
     * - 다음 액션 유도
     * - 친근한 톤앤매너
     */
    @androidx.compose.runtime.Composable()
    public static final void EmptyStateView(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    java.lang.String actionLabel, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAction) {
    }
    
    @androidx.compose.runtime.Composable()
    @kotlin.Suppress(names = {"unused"})
    public static final void LoadingView(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    @androidx.compose.runtime.Composable()
    @kotlin.Suppress(names = {"unused"})
    public static final void ErrorView(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    java.lang.String message, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onRetry) {
    }
}