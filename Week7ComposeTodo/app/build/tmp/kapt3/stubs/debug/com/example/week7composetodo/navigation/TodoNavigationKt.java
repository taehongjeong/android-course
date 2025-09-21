package com.example.week7composetodo.navigation;

@kotlin.Metadata(mv = {2, 2, 0}, k = 2, xi = 48, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u00a8\u0006\u0002"}, d2 = {"TodoNavigation", "", "app_debug"})
public final class TodoNavigationKt {
    
    /**
     * Todo 앱의 네비게이션 시스템 - Navigation Compose 구현
     *
     * Navigation Compose의 장점:
     * 1. 타입 안전성: 컴파일 타임에 경로와 파라미터 검증
     * 2. 단순한 API: XML 없이 Kotlin 코드로 네비게이션 정의
     * 3. Jetpack Compose 통합: Composable 함수와 자연스러운 연동
     * 4. ViewModel 공유: 화면 간 ViewModel 쉽게 공유 가능
     *
     * 네비게이션 아키텍처:
     * - Single Activity Pattern: 하나의 Activity 내에서 여러 Composable 화면 전환
     * - Fragment 없이 순수 Composable로 구성
     * - 백스택 자동 관리로 뒤로가기 버튼 지원
     *
     * @Composable 함수인 이유:
     * - Compose UI 트리의 일부로 포함
     * - remember, State 등 Compose 기능 활용 가능
     * - UI 변경 시 자동 리컴포지션 지원
     */
    @androidx.compose.runtime.Composable()
    public static final void TodoNavigation() {
    }
}