package com.example.week7composetodo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.week7composetodo.ui.screen.TodoDetailScreen
import com.example.week7composetodo.ui.screen.TodoListScreen
import com.example.week7composetodo.viewmodel.TodoViewModel

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
@Composable
fun TodoNavigation() {
    /**
     * NavController 생성 및 기억
     * 
     * rememberNavController():
     * - 네비게이션 상태를 관리하는 컨트롤러 생성
     * - remember로 리컴포지션 시에도 인스턴스 유지
     * - 백스택, 현재 화면, 네비게이션 이력 관리
     * 
     * NavController의 역할:
     * - 화면 전환 명령 처리 (navigate, popBackStack 등)
     * - 현재 화면 정보 제공
     * - 딥링크 처리
     * - 네비게이션 애니메이션 제어
     */
    val navController = rememberNavController()
    
    /**
     * NavHost - 네비게이션 그래프의 컨테이너
     * 
     * NavHost 역할:
     * - 모든 가능한 목적지(destination) 정의
     * - 시작 화면 지정
     * - 경로별 Composable 매핑
     * 
     * 매개변수:
     * - navController: 네비게이션 제어를 위한 컨트롤러
     * - startDestination: 앱 실행 시 첫 번째로 표시될 화면
     */
    NavHost(
        navController = navController,
        startDestination = "todo_list"
    ) {
        /**
         * Todo 목록 화면 - 메인 화면
         * 
         * composable() 함수:
         * - 특정 경로(route)에 Composable 함수를 연결
         * - 사용자가 해당 경로로 이동 시 지정된 Composable 실행
         * 
         * 경로 "todo_list":
         * - 단순한 문자열 경로
         * - 파라미터가 없는 정적 경로
         * - 앱의 홈 화면 역할
         */
        composable("todo_list") {
            /**
             * Hilt ViewModel 주입
             * 
             * hiltViewModel<TodoViewModel>():
             * - Hilt가 자동으로 ViewModel 인스턴스 생성
             * - Repository 의존성도 자동 주입
             * - 화면 구성 변경 시에도 인스턴스 유지
             * 
             * ViewModel 공유:
             * - 같은 네비게이션 스코프 내에서 ViewModel 공유 가능
             * - 화면 간 데이터 전달이나 상태 공유에 활용
             */
            val viewModel: TodoViewModel = hiltViewModel()
            
            /**
             * TodoListScreen Composable 실행
             * 
             * 매개변수:
             * - viewModel: 주입받은 ViewModel 전달
             * - onNavigateToDetail: 상세 화면으로 이동하는 콜백 함수
             * 
             * 콜백 함수 패턴:
             * - UI 이벤트를 상위 컴포넌트로 전달
             * - 네비게이션 로직을 한 곳에서 관리
             * - 컴포넌트 재사용성 향상
             */
            TodoListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { todoId ->
                    /**
                     * 상세 화면 네비게이션 로직
                     * 
                     * todoId가 null인 경우:
                     * - 새로운 Todo 추가 모드
                     * - "todo_detail/new" 경로로 이동
                     * 
                     * todoId가 있는 경우:
                     * - 기존 Todo 편집 모드  
                     * - "todo_detail/{실제ID}" 경로로 이동
                     * 
                     * navController.navigate():
                     * - 지정된 경로로 이동
                     * - 백스택에 현재 화면 추가
                     * - 뒤로가기 시 이전 화면으로 복귀
                     */
                    if (todoId != null) {
                        navController.navigate("todo_detail/$todoId")
                    } else {
                        navController.navigate("todo_detail/new")
                    }
                }
            )
        }
        
        /**
         * Todo 상세/편집 화면 - 동적 파라미터 포함
         * 
         * 동적 경로 정의:
         * - "todo_detail/{todoId}": 중괄호로 파라미터 표시
         * - todoId 부분이 실제 값으로 대체됨
         * - 예: "todo_detail/123", "todo_detail/new"
         * 
         * arguments 리스트:
         * - 경로 파라미터의 타입과 속성 정의
         * - 컴파일 타임 타입 안전성 제공
         * - nullable, defaultValue 등 설정 가능
         */
        composable(
            route = "todo_detail/{todoId}",
            arguments = listOf(
                /**
                 * navArgument 정의
                 * 
                 * "todoId":
                 * - 파라미터 이름 (경로의 {todoId}와 일치)
                 * - 대소문자 구분
                 * 
                 * NavType.StringType:
                 * - 파라미터 타입 지정
                 * - String, Int, Long, Boolean 등 지원
                 * - 타입 안전성과 자동 변환 제공
                 * 
                 * 추가 설정 가능:
                 * - nullable = true: null 값 허용
                 * - defaultValue: 기본값 설정
                 */
                navArgument("todoId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            /**
             * ViewModel 주입 및 파라미터 추출
             * 
             * backStackEntry:
             * - 현재 네비게이션 엔트리 정보
             * - 경로 파라미터, 쿼리 파라미터 포함
             * - 이전 화면으로부터 전달된 데이터 접근
             * 
             * arguments?.getString("todoId"):
             * - 경로에서 todoId 파라미터 추출
             * - null 가능 (파라미터가 없거나 타입 불일치 시)
             * - NavType에서 지정한 타입으로 자동 변환
             */
            val viewModel: TodoViewModel = hiltViewModel()
            val todoId = backStackEntry.arguments?.getString("todoId")
            
            /**
             * TodoDetailScreen Composable 실행
             * 
             * todoId 처리 로직:
             * - "new": 새 Todo 추가 모드 (null 전달)
             * - 실제 ID: 편집 모드 (ID 전달)
             * 
             * 이런 처리가 필요한 이유:
             * - 하나의 화면으로 추가/편집 모드 모두 처리
             * - 코드 중복 제거
             * - 일관된 사용자 경험 제공
             */
            TodoDetailScreen(
                todoId = if (todoId == "new") null else todoId,
                viewModel = viewModel,
                onNavigateBack = {
                    /**
                     * 뒤로가기 네비게이션
                     * 
                     * navController.popBackStack():
                     * - 백스택에서 현재 화면 제거
                     * - 이전 화면으로 복귀
                     * - 시스템 뒤로가기 버튼과 동일한 동작
                     * 
                     * 사용 사례:
                     * - 취소 버튼 클릭
                     * - 저장 완료 후 목록으로 돌아가기
                     * - 커스텀 뒤로가기 버튼
                     */
                    navController.popBackStack()
                }
            )
        }
    }
}