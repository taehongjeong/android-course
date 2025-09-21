package com.example.week7composetodo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Todo 앱의 메인 Application 클래스
 * 
 * 주요 역할:
 * 1. Hilt 의존성 주입을 위한 진입점 역할
 * 2. 앱 전체의 생명주기 관리
 * 3. 전역 설정 및 초기화 담당
 * 
 * @HiltAndroidApp 어노테이션:
 * - Hilt의 코드 생성을 트리거하는 어노테이션
 * - Application 레벨에서 Hilt 컨테이너를 생성
 * - 앱 전체에서 의존성 주입을 사용할 수 있게 해줌
 * 
 * Hilt DI (Dependency Injection) 장점:
 * - 객체 생성과 관리를 자동화
 * - 테스트 시 모킹 용이
 * - 코드의 결합도 감소, 유지보수성 향상
 */
@HiltAndroidApp
class TodoApplication : Application()