package com.example.mypomodorotimer

/**
 * 타이머의 상태를 나타내는 enum class
 * Activity 생명주기와 함께 상태를 관리하는데 사용됨
 */
enum class TimerState {
    STOPPED,  // 타이머가 중지된 상태
    PAUSED,   // 타이머가 일시정지된 상태
    RUNNING   // 타이머가 실행 중인 상태
}