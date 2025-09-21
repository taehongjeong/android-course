package com.example.week7composetodo.ui.component;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u001aD\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u001a\u001a\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007\u001a,\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u00a8\u0006\u0012"}, d2 = {"TodoItem", "", "modifier", "Landroidx/compose/ui/Modifier;", "todo", "Lcom/example/week7composetodo/data/Todo;", "onToggleComplete", "Lkotlin/Function0;", "onDelete", "onClick", "PriorityChip", "priority", "Lcom/example/week7composetodo/data/Priority;", "DeleteConfirmationDialog", "todoTitle", "", "onConfirm", "onDismiss", "app_debug"})
public final class TodoItemKt {
    
    /**
     * TodoItem - 재사용 가능한 Todo 항목 컴포넌트
     *
     * 재사용 가능한 Composable의 특징:
     * 1. 단일 책임: 하나의 Todo 항목만 표시
     * 2. 상태 없음: 모든 데이터를 매개변수로 받음
     * 3. 이벤트 위임: 클릭, 삭제 등 이벤트를 콜백으로 전달
     * 4. 커스터마이징 가능: Modifier를 통한 외관 조정
     *
     * Material Design 적용:
     * - SwipeToDismiss 제스처
     * - 애니메이션 효과
     * - Material3 컬러 시스템
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void TodoItem(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Todo todo, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggleComplete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PriorityChip(@org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Priority priority, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DeleteConfirmationDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String todoTitle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onConfirm, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}