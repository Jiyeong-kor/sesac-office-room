package com.sesac.officeroom.viewmodel

import com.sesac.officeroom.data.MeetingRoom
import com.sesac.officeroom.view.Input
import com.sesac.officeroom.view.Strings
import com.sesac.officeroom.view.View


/**
 * Office manager
 *
 */
class OfficeManager {

    /**
     * 메인 process
     */
    fun mainProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.MAIN_MESSAGE)

            when (Input.isInt()) {
                1 -> manageOfficeRoomProcess()
                2 -> manageSalesProcess()
                0 -> break
                else -> View.prettyPrintConsole(Strings.ERROR_MESSAGE)
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리
     *
     * desc: 회의실 관리 process
     */
    private fun manageOfficeRoomProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_1_MENU_MESSAGE)

            when (Input.isInt()) {
                1 -> roomReservationProcess()
                2 -> officeRoomInfoProcess()
                3 -> reservationInfoProcess()
                0 -> break
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process
     */
    private fun roomReservationProcess() {
        View.prettyPrintConsole(showAvailableRooms() //예약이 가능한 회의실을 보여줌
        + "\n" + Strings.STEP_1_1_ROOM_CHOOSE) //예약을 원하는 회의실 번호를 입력하라는 메시지
        val roomNumber = Input.isInt() //예약 받은 회의실 번호를 roomNumber에 담음
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 예약이 가능한 회의실을 '보여주기'만 하는 기능
     */
    private fun showAvailableRooms(): String {
        View.prettyPrintConsole(Strings.STEP_1_1_MESSAGE_1)//인원 수 입력
        val capacity = Input.isInt()

        View.prettyPrintConsole(Strings.STEP_1_1_MESSAGE_2)//창문 필요 여부 입력
        val window = when (Input.isInt()) {
            1 -> true
            else -> false
        }

        View.prettyPrintConsole(Strings.STEP_1_1_MESSAGE_3)//포토 부스 필요 여부 입력
        val photoBooth = when (Input.isInt()) {
            1 -> true
            else -> false
        }
        //TODO: 입력 잘못 받았을 경우 예외처리 해주기

        val result = StringBuilder()
        MeetingRoom.entries.forEach { room ->
            getAvailableRoomInfo(room, capacity, window, photoBooth)?.let { formattedRoomInfo ->
                result.append(formattedRoomInfo).append("\n") // getAvailableRoomInfo 함수를 통해 조건에 해당하는 회의실 정보를 result에 추가
            }
        }
        return if (result.isEmpty()) {
            Strings.STEP_1_1_NO_ROOM_FOUND // 조건에 해당하는 회의실이 없을 경우 보여줄 메시지
        } else {
            result.toString().trim() // 해당하는 조건의 회의실들을 return해줌
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process 중 조건에 맞는 회의실 필터링
     */

    private fun getAvailableRoomInfo(
        room: MeetingRoom,
        capacity: Int,
        window: Boolean,
        photoBooth: Boolean
    ): String? { //window와 photoBooth가 false여도 true에 해당하는 회의실을 반환함과 동시에 window나 photoBooth가 true인 경우에는 true인 것만 반환하도록 하는 조건식
        if (capacity <= room.maxCapacity && (!window || room.hasWindow) && (!photoBooth || room.hasPhotoBooth)) {
            val windowInfo = if (room.hasWindow) "있음" else "없음"
            val photoBoothInfo = if (room.hasPhotoBooth) "있음" else "없음"

            return String.format(
                Strings.STEP_1_1_ROOM_INFO,
                room.roomNumber,
                room.roomName,
                room.baseCapacity,
                room.maxCapacity,
                room.baseCostPerHour,
                room.additionalCostPerPerson,
                windowInfo,
                photoBoothInfo
            )
        }
        return null
    }

    /**
     * 메인 > [1]회의실 관리 > [2]회의실 정보 확인
     *
     * desc: 회의실 정보 확인 process
     */
    private fun officeRoomInfoProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_1_2_MESSAGE)

            when (Input.isInt()) {
                1 -> {}
                2 -> {}
                0 -> break
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [3]회의실 예약내역 조회
     *
     * desc: 회의실 정보 확인 process
     */
    private fun reservationInfoProcess() {
        View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
    }

    /**
     * 메인 > [2]매출 관리
     *
     * desc: 매출 관리 process
     */
    private fun manageSalesProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_2_MENU_MESSAGE)

            when (Input.isInt()) {
                1 -> {}
                2 -> {}
                3 -> {}
                4 -> {}
                0 -> break
            }
        }
    }
}