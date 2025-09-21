package com.example.week7composetodo.data;

/**
 * Room 타입 변환기 클래스
 *
 * TypeConverter 사용 이유:
 * 1. Room은 SQLite를 기반으로 하므로 기본 타입(Int, String, Boolean 등)만 지원
 * 2. enum, Date, 커스텀 클래스 등을 저장하려면 변환기 필요
 * 3. 자동으로 변환이 이루어져 개발자는 커스텀 타입을 자연스럽게 사용 가능
 *
 * @TypeConverter 어노테이션:
 * - Room에게 이 메서드가 타입 변환용임을 알림
 * - 자동으로 변환이 이루어지도록 설정
 */
@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0007J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0006\u001a\u00020\u0005H\u0007\u00a8\u0006\t"}, d2 = {"Lcom/example/week7composetodo/data/Converters;", "", "<init>", "()V", "fromPriority", "", "priority", "Lcom/example/week7composetodo/data/Priority;", "toPriority", "app_debug"})
@kotlin.Suppress(names = {"unused"})
public final class Converters {
    
    public Converters() {
        super();
    }
    
    /**
     * Priority enum을 String으로 변환 (데이터베이스 저장용)
     *
     * enum.name 사용 이유:
     * - enum 상수의 이름을 문자열로 반환
     * - 안정적이고 가독성이 좋음
     * - ordinal() 대신 name 사용으로 enum 순서 변경에 안전
     */
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String fromPriority(@org.jetbrains.annotations.NotNull()
    com.example.week7composetodo.data.Priority priority) {
        return null;
    }
    
    /**
     * String을 Priority enum으로 변환 (데이터베이스에서 읽어올 때)
     *
     * valueOf() 메서드:
     * - 문자열을 해당 enum 상수로 변환
     * - 잘못된 문자열의 경우 IllegalArgumentException 발생
     * - 데이터 무결성 보장
     */
    @androidx.room.TypeConverter()
    @org.jetbrains.annotations.NotNull()
    public final com.example.week7composetodo.data.Priority toPriority(@org.jetbrains.annotations.NotNull()
    java.lang.String priority) {
        return null;
    }
}