package com.example.week7composetodo.di

import android.content.Context
import androidx.room.Room
import com.example.week7composetodo.data.TodoDao
import com.example.week7composetodo.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 데이터베이스 모듈 - 의존성 주입 설정
 * 
 * Hilt Module의 역할:
 * 1. 의존성 주입을 위한 객체 생성 방법 정의
 * 2. 생명주기 관리와 스코프 제어
 * 3. 인터페이스와 구현체 바인딩
 * 4. 싱글톤 패턴 자동 관리
 * 
 * @Module 어노테이션:
 * - Hilt에게 이 클래스가 의존성 주입 모듈임을 알림
 * - @Provides 메서드들을 포함하여 객체 생성 방법 제공
 * 
 * @InstallIn(SingletonComponent::class):
 * - 이 모듈을 SingletonComponent에 설치
 * - Application 생명주기와 동일하게 관리
 * - 앱 전체에서 동일한 인스턴스 공유
 * 
 * object 키워드 사용:
 * - 코틀린의 싱글톤 객체
 * - 인스턴스가 하나만 존재함을 보장
 * - 메소드만 제공하고 상태를 갖지 않음
 */
@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object DatabaseModule {
    
    /**
     * TodoDatabase 인스턴스 제공 메서드
     * 
     * @Provides 어노테이션:
     * - Hilt에게 이 메서드가 의존성을 제공함을 알림
     * - TodoDatabase 타입이 필요할 때 이 메서드 호출
     * 
     * @Singleton 어노테이션:
     * - 앱 전체에서 하나의 인스턴스만 생성
     * - 데이터베이스 연결 비용 절약과 데이터 일관성 보장
     * 
     * @ApplicationContext 어노테이션:
     * - Application Context를 주입받음
     * - Activity Context와 달리 메모리 누수 방지
     * - 데이터베이스는 Application 생명주기와 동일해야 안전
     */
    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"  // 데이터베이스 파일 이름
        ).build()
    }
    
    /**
     * TodoDao 인스턴스 제공 메서드
     * 
     * 의존성 체인:
     * - TodoDao를 요청하면 Hilt가 자동으로 TodoDatabase를 먼저 생성
     * - TodoDatabase에서 todoDao() 메서드를 호출하여 DAO 인스턴스 반환
     * 
     * @Singleton 어노테이션:
     * - DAO도 싱글톤으로 관리하여 데이터 일관성 보장
     * - 동일한 Database 인스턴스에서 생성된 DAO 공유
     * 
     * 매개변수 database: TodoDatabase:
     * - Hilt가 자동으로 provideTodoDatabase() 결과를 주입
     * - 의존성 그래프에서 자동 해결
     */
    @Provides
    @Singleton
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }
}