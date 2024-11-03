package com.sesac.officeroom.newproject.presentation.view

import com.sesac.officeroom.data.MeetingRoom
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.newproject.presentation.viewmodel.ManageOfficeViewModel
import com.sesac.officeroom.newproject.repository.ManageOfficeRepositoryImpl
import com.sesac.officeroom.oldproject.presentation.common.Input
import com.sesac.officeroom.oldproject.presentation.common.Strings
import com.sesac.officeroom.oldproject.presentation.common.View
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime

/**
 * 메인 > [1]회의실 관리
 *
 * desc: 회의실 관리 process
 */
class ManageOfficeView {
    val viewModel = ManageOfficeViewModel(ManageOfficeRepositoryImpl())

    fun main() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_1_MENU_MESSAGE)
            when(Input.isInt()) {
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

        //예약이 가능한 회의실을 보여줌
        View.prettyPrintConsole(
            showAvailableRooms()

                    + Strings.NEW_LINE

                    //예약을 원하는 회의실 번호를 입력하라는 메시지
                    + Strings.STEP_1_1_ROOM_CHOOSE
        )

        //예약 받은 회의실 번호를 roomNumber에 담음
        val roomNumber = Input.isInt()


        // 저장 예제 참고
        /*runBlocking {
            val date = LocalDate.of(2024, 11, 1)
            val time = LocalTime.of(9, 0)
            val tempDTO = ReservationDTO(
                1,
                date,
                time,
                1,
                4,
                "01077300328"
            )
            viewModel.makeReservation(tempDTO)
        }*/

    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 예약이 가능한 회의실을 '보여주기'만 하는 기능
     */
    private fun showAvailableRooms(): String {

        View.prettyPrintConsole(Strings.STEP_1_1_HEADER_MESSAGE)
        //인원 수 입력
        print(Strings.STEP_1_1_MESSAGE_1)
        val capacity = Input.isInt()

        //창문 필요 여부 입력
        print(Strings.STEP_1_1_MESSAGE_2)
        val needWindow = when (Input.isInt()) {
            1 -> true
            else -> false
        }

        //포토 부스 필요 여부 입력
        print(Strings.STEP_1_1_MESSAGE_3)
        val needPhotoBooth = when (Input.isInt()) {
            1 -> true
            else -> false
        }

        //TODO: 입력 잘못 받았을 경우 예외처리 해주기
        //mapNotNull은 MeetingRooms.entries를 돌면서 getAvailableRoomInfo 함수의 반환값이 null이 아닌 경우에만 처리함

        // 사무실 목록 불러오기
        /*runBlocking {
            val officeList = viewModel.getOfficeList()

            // TODO: MeetingRoom 을 officeList 로 바꿔주기
            val result = MeetingRoom.entries.mapNotNull { room ->
                getAvailableRoomInfo(room, capacity, needWindow, needPhotoBooth)
                //필터링 된 회의실들을 받아서 줄 단위로 연결함
            }.joinToString(separator = Strings.NEW_LINE)
        }*/

        val result = MeetingRoom.entries.mapNotNull { room ->
            getAvailableRoomInfo(room, capacity, needWindow, needPhotoBooth)
            //필터링 된 회의실들을 받아서 줄 단위로 연결함
        }.joinToString(separator = Strings.NEW_LINE)

        return if (result.isEmpty()) {
            // 조건에 해당하는 회의실이 없을 경우 보여줄 메시지
            Strings.STEP_1_1_NO_ROOM_FOUND
        } else {
            // 해당하는 조건의 회의실들을 return해 줌
            result
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
        needWindow: Boolean,
        needPhotoBooth: Boolean
    ): String? {
        //window와 photoBooth가 false여도 true에 해당하는 회의실을 반환함과 동시에
        // window나 photoBooth가 true인 경우에는 true인 것만 반환하도록 하는 조건식
        if (capacity <= room.maxCapacity && (!needWindow || room.hasWindow) && (!needPhotoBooth || room.hasPhotoBooth)) {
            val windowInfo = if (room.hasWindow) Strings.YES_MESSAGE else Strings.NO_MESSAGE
            val photoBoothInfo = if (room.hasPhotoBooth) Strings.YES_MESSAGE else Strings.NO_MESSAGE

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

            when(Input.isInt()) {
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


    fun test() {
        // office 목록 불러오기
        runBlocking {
            val officeList = viewModel.getOfficeList()
            officeList.listIterator().forEach {
                println(it)
            }
        }

        // 예약 데이터 저장
        runBlocking {
            val date = LocalDate.of(2024, 11, 1)
            val time = LocalTime.of(9, 0)
            val tempDTO = ReservationDTO(
                1,
                date,
                time,
                1,
                4,
                "01077300328"
            )
            viewModel.makeReservation(tempDTO)
        }
    }

    fun test2(){
        //Reservations.txt에 제대로 데이터가 파싱되었는지 확인하는 함수
        runBlocking {
            val reservationList = viewModel.getReservationList()
            reservationList.listIterator().forEach {
                println(it)
            }
        }
    }
}