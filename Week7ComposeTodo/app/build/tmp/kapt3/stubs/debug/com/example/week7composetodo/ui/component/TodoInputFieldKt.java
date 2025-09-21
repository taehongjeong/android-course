package com.example.week7composetodo.ui.component;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u008b\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\u0015\b\u0002\u0010\u0011\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0012\u00a2\u0006\u0002\b\u00132\b\b\u0002\u0010\u0014\u001a\u00020\n2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0003H\u0007\u001a&\u0010\u0016\u001a\u00020\u00012\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a.\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a&\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\n2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0012H\u0003\u00a8\u0006 "}, d2 = {"TodoInputField", "", "value", "", "onValueChange", "Lkotlin/Function1;", "label", "modifier", "Landroidx/compose/ui/Modifier;", "singleLine", "", "maxLines", "", "keyboardOptions", "Landroidx/compose/foundation/text/KeyboardOptions;", "keyboardActions", "Landroidx/compose/foundation/text/KeyboardActions;", "trailingIcon", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "isError", "errorMessage", "QuickAddTodo", "onAddTodo", "PrioritySelector", "selectedPriority", "Lcom/example/week7composetodo/data/Priority;", "onPrioritySelected", "PriorityFilterChip", "priority", "selected", "onClick", "app_debug"})
public final class TodoInputFieldKt {
    
    /**
     * TodoInputField - 커스터마이징된 입력 필드 컴포넌트
     *
     * 컴포넌트 래핑의 이유:
     * 1. 일관성: 앱 전체에서 동일한 입력 필드 스타일
     * 2. 기능 확장: 기본 TextField에 추가 기능 통합
     * 3. 재사용성: 공통 기능(클리어 버튼, 에러 처리 등)
     * 4. 유지보수: 한 곳에서 스타일과 동작 관리
     *
     * 추가된 기능:
     * - 자동 클리어 버튼
     * - 에러 상태 처리
     * - 키보드 옵션 설정
     * - 접근성 개선
     */
    @androidx.compose.runtime.Composable()
    public static final void TodoInputField(@org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, boolean singleLine, int maxLines, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.text.KeyboardOptions keyboardOptions, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.text.KeyboardActions keyboardActions, @org.jetbrains.annotations.Nullable()
    androidx.compose.runtime.internal.ComposableFunction0<kotlin.Unit> trailingIcon, boolean isError, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
    }
    
    @androidx.compose.runtime.Composable()
    @kotlin.Suppress(names = {"unused"})
    public static final void QuickAddTodo(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onAddTodo, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PrioritySelector(@org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Priority selectedPriority, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.week7composetodo.data.Priority, kotlin.Unit> onPrioritySelected, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PriorityFilterChip(com.example.week7composetodo.data.Priority priority, boolean selected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}