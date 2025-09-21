package com.example.week7composetodo.data;

/**
 * Todo 우선순위를 나타내는 열거형
 *
 * enum class 사용 장점:
 * 1. 타입 안전성: 정의된 값만 사용 가능
 * 2. 가독성: 코드에서 의미가 명확함
 * 3. 유지보수: 새로운 우선순위 추가 시 컴파일러가 누락 체크
 *
 * 우선순위 단계:
 * - LOW: 낮은 우선순위 (여유가 있을 때 처리)
 * - NORMAL: 보통 우선순위 (기본값)
 * - HIGH: 높은 우선순위 (우선적으로 처리)
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/week7composetodo/data/Priority;", "", "<init>", "(Ljava/lang/String;I)V", "LOW", "NORMAL", "HIGH", "QUICK", "app_debug"})
public enum Priority {
    /*public static final*/ LOW /* = new LOW() */,
    /*public static final*/ NORMAL /* = new NORMAL() */,
    /*public static final*/ HIGH /* = new HIGH() */,
    /*public static final*/ QUICK /* = new QUICK() */;
    
    Priority() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.week7composetodo.data.Priority> getEntries() {
        return null;
    }
}