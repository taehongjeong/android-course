package com.example.week7composetodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Todo 앱의 Room 데이터베이스 정의
 * 
 * Room Database 아키텍처:
 * 1. Database 클래스: 데이터베이스 설정과 DAO 접근점
 * 2. Entity 클래스: 테이블 구조 정의
 * 3. DAO 인터페이스: 데이터베이스 연산 메서드
 * 
 * @Database 어노테이션 매개변수:
 * - entities: 데이터베이스에 포함될 엔티티 목록
 * - version: 데이터베이스 스키마 버전 (마이그레이션 시 사용)
 * - exportSchema: 스키마 내보내기 여부 (false로 설정하여 빌드 간단화)
 */
@Database(
    entities = [Todo::class],
    version = 1,
    exportSchema = false
)
/**
 * @TypeConverters: 커스텀 타입 변환기 등록
 * Room은 기본 타입만 지원하므로, enum 같은 커스텀 타입을 위해 변환기 필요
 */
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    
    /**
     * TodoDao 인스턴스를 반환하는 추상 메서드
     * 
     * abstract 사용 이유:
     * - Room이 컴파일 시에 실제 구현체를 자동 생성
     * - 개발자는 메서드 시그니처만 정의하고 구현은 Room에 위임
     */
    abstract fun todoDao(): TodoDao
}

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
@Suppress("unused")
class Converters {
    
    /**
     * Priority enum을 String으로 변환 (데이터베이스 저장용)
     * 
     * enum.name 사용 이유:
     * - enum 상수의 이름을 문자열로 반환
     * - 안정적이고 가독성이 좋음
     * - ordinal() 대신 name 사용으로 enum 순서 변경에 안전
     */
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }
    
    /**
     * String을 Priority enum으로 변환 (데이터베이스에서 읽어올 때)
     * 
     * valueOf() 메서드:
     * - 문자열을 해당 enum 상수로 변환
     * - 잘못된 문자열의 경우 IllegalArgumentException 발생
     * - 데이터 무결성 보장
     */
    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}