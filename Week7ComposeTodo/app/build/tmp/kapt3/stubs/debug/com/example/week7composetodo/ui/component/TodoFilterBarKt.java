package com.example.week7composetodo.ui.component;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a6\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u0010\b\u001a\u00020\tH\u0007\u001a.\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u0011H\u0003\u001a\u001a\u0010\u0012\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0003\u001a3\u0010\u0013\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0003\u00a2\u0006\u0004\b\u0019\u0010\u001a\u00a8\u0006\u001b"}, d2 = {"TodoFilterBar", "", "modifier", "Landroidx/compose/ui/Modifier;", "selectedFilter", "Lcom/example/week7composetodo/viewmodel/TodoFilter;", "onFilterSelected", "Lkotlin/Function1;", "todoCount", "Lcom/example/week7composetodo/ui/component/TodoCount;", "TodoFilterChip", "filter", "selected", "", "count", "", "onClick", "Lkotlin/Function0;", "TodoStatistics", "StatisticItem", "label", "", "value", "color", "Landroidx/compose/ui/graphics/Color;", "StatisticItem-g2O1Hgs", "(Landroidx/compose/ui/Modifier;Ljava/lang/String;Ljava/lang/String;J)V", "app_debug"})
public final class TodoFilterBarKt {
    
    /**
     * TodoFilterBar - 필터링과 통계를 담당하는 컴포넌트
     *
     * 컴포넌트 설계 원칙:
     * 1. 관심사 분리: 필터링 UI만 담당
     * 2. 데이터 기반: 외부에서 받은 데이터로만 UI 구성
     * 3. 상태 호이스팅: 필터 상태를 상위에서 관리
     * 4. 애니메이션 통합: 부드러운 사용자 경험
     *
     * Material3 Filter Chip 활용:
     * - 선택 상태 시각화
     * - 개수 배지 표시
     * - 접근성 지원
     */
    @androidx.compose.runtime.Composable()
    public static final void TodoFilterBar(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier, @org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.viewmodel.TodoFilter selectedFilter, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.week7composetodo.viewmodel.TodoFilter, kotlin.Unit> onFilterSelected, @org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.ui.component.TodoCount todoCount) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TodoFilterChip(com.example.week7composetodo.viewmodel.TodoFilter filter, boolean selected, int count, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TodoStatistics(com.example.week7composetodo.ui.component.TodoCount todoCount, androidx.compose.ui.Modifier modifier) {
    }
}