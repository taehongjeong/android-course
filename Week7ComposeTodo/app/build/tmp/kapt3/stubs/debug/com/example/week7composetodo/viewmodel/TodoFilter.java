package com.example.week7composetodo.viewmodel;

/**
 * Todo 필터링 옵션을 나타내는 열거형
 *
 * enum class 사용 이유:
 * - 타입 안전성: 미리 정의된 값만 사용 가능
 * - 가독성: 코드에서 의미가 명확함
 * - 유지보수: 새로운 필터 추가 시 컴파일러가 누락 체크
 *
 * 필터 종류:
 * - ALL: 모든 Todo 표시 (기본값)
 * - ACTIVE: 완료되지 않은 Todo만 표시 (isCompleted = false)
 * - COMPLETED: 완료된 Todo만 표시 (isCompleted = true)
 *
 * UI에서 사용:
 * - 필터 바의 탭/버튼에 연결
 * - ViewModel의 combine() 함수에서 필터링 로직에 활용
 * - 사용자가 선택한 필터에 따라 자동으로 목록 업데이트
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/week7composetodo/viewmodel/TodoFilter;", "", "<init>", "(Ljava/lang/String;I)V", "ALL", "ACTIVE", "COMPLETED", "app_debug"})
public enum TodoFilter {
    /*public static final*/ ALL /* = new ALL() */,
    /*public static final*/ ACTIVE /* = new ACTIVE() */,
    /*public static final*/ COMPLETED /* = new COMPLETED() */;
    
    TodoFilter() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.week7composetodo.viewmodel.TodoFilter> getEntries() {
        return null;
    }
}