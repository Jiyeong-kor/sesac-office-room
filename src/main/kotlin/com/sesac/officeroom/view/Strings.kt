package com.sesac.officeroom.view

object Strings {
    const val ERROR_MESSAGE = "잘못된 값입니다. 다시 입력해주세요."
    const val MAIN_MESSAGE = "[1]회의실 관리 [2]매출 관리 [0]종료"

    // 회의실 관리
    const val STEP_1_MENU_MESSAGE = "[1]회의실 예약하기 [2]회의실 정보 확인 [3]회의실 예약 내역 조회\n[0]메인 메뉴로 돌아가기"

    const val STEP_1_1_MESSAGE_1 = "회의실 예약을 진행합니다." + "\n인원 수를 입력해주세요: "
    const val STEP_1_1_MESSAGE_2 = "\n창문이 필요하신가요? ([예]: 1, [아니오]: 2) "
    const val STEP_1_1_MESSAGE_3 = "\n촬영 부스가 필요하신가요? ([예]: 1, [아니오]: 2) "
    const val STEP_1_1_ROOM_INFO = "%d. [%s]: 기준 인원 %d명, 최대 인원 %d명, 기본 요금 %d원, 인당 추가 요금 %d원, 창문 %s, 포토부스 %s"
    const val STEP_1_1_NO_ROOM_FOUND = "해당하는 회의실이 없습니다."
    const val STEP_1_1_ROOM_CHOOSE = "예약을 원하는 회의실 번호를 입력해주세요."

    const val STEP_1_2_MESSAGE = "[1]회의실 목록 조회 [2]회의실 별 예약현황 조회 [0]메뉴로 돌아가기"

    const val STEP_1_3_MESSAGE = "하이픈(-)을 제외한 예약자의 휴대폰번호를 입력해주세요."
    const val STEP_1_3_MENU_MESSAGE = "[1]예약취소 [2]공유하기 [0]메뉴로 돌아가기"

    // 매출 관리
    const val STEP_2_MENU_MESSAGE = "[1]총 매출 조회 [2]회의실 별 매출조회 [3]날짜별 매출조회 [4]총 이용자 수 조회\n[0]메인 메뉴로 돌아가기"
    const val STEP_2_1_MESSAGE = "현재까지의 총 매출은 %d원 입니다."
    const val STEP_2_2_MESSAGE = ""
    const val STEP_2_3_MESSAGE = ""
    const val STEP_2_4_MESSAGE = "현재까지의 총 이용자 수는 %d명 입니다."

}
