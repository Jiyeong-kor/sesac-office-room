package com.sesac.officeroom.presentation.view

import com.sesac.officeroom.data.officeList
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
import com.sesac.officeroom.presentation.common.View
import com.sesac.officeroom.repository.OfficeManagerRepositoryImpl
import com.sesac.officeroom.presentation.viewmodel.OfficeManagerViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime


class OfficeManagerView {
    private val viewModel = OfficeManagerViewModel(OfficeManagerRepositoryImpl())

    /**
     * 메인 process
     */
    fun mainProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.MAIN_MESSAGE)

            when(Input.isInt()) {
                1 -> manageOfficeRoomProcess()
                2 -> manageSalesProcess()
                -1 -> test()
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
        //조건을 입력받고 예약이 가능한 회의실을 보여줌
        val (availableRoomResult, numberOfPeople) = showAvailableRooms()
        //예약이 가능한 회의실이 존재하는지를 확인
        if(doesTheRoomExist(availableRoomResult)) {
            val officeId = Input.isInt()
            //TODO: id가 있는 회의실 값만 입력받고 없는 경우 예외 처리
            //희망 이용 시간 입력
            View.prettyPrintConsole(Strings.STEP_1_1_USAGE_TIME_CHOOSE)
            val usageTime = Input.isInt()
            //휴대폰 번호 입력
            View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
            val phoneNumber = Input.isString()
            //입력 정보 저장
            reserveDataOfRoom(officeId,usageTime, numberOfPeople, phoneNumber)
            //TODO: 저장 기능이 잘 구현 된 건지 확인 부탁드립니다
        }
    }
    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process 중 데이터를 저장하는 기능
     */
    private fun reserveDataOfRoom(officeId: Int, usageTime: Int, numberOfPeople: Int, phoneNumber: String) {
        runBlocking {
            val date = LocalDate.of(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
            val time = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)
            val tempDTO = ReservationDTO(
                officeId,
                date,
                time,
                usageTime,
                numberOfPeople,
                phoneNumber
            )
            viewModel.makeReservation(tempDTO)
        }
    }
    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 조건에 맞는 회의실이 존재하는 지를 Boolean 값으로 알려줌
     */
    private fun doesTheRoomExist(availableRoomResult: String): Boolean {
        if (availableRoomResult.isEmpty()) {
            // 조건에 해당하는 회의실이 없을 경우 보여줄 메시지
            View.prettyPrintConsole(Strings.STEP_1_1_NO_ROOM_FOUND)
            return false
        } else {
            // 해당하는 조건의 회의실들을 return해 줌
            //예약을 원하는 회의실 번호를 입력하라는 메시지
            View.prettyPrintConsole(Strings.STEP_1_1_ROOM_CHOOSE)
            println(availableRoomResult)
            return true
        }

    }
    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 예약이 가능한 회의실을 '보여주기'만 하는 기능
     */
    //TODO: showAvailableRooms()라는 이름이 정확한 기능과 같지 않아서 이름 변경 필요함
    private fun showAvailableRooms(): Pair<String, Int> {

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
        val needFilmBooth = when (Input.isInt()) {
            1 -> true
            else -> false
        }

        //TODO: 입력 잘못 받았을 경우 예외처리 해주기
        //mapNotNull은 officeList.entries를 돌면서 getAvailableRoomInfo 함수의 반환값이 null이 아닌 경우에만 처리함

        // 사무실 목록 불러오기
        /*runBlocking {
            val officeList = viewModel.getOfficeList()

            // TODO: MeetingRoom 을 officeList 로 바꿔주기 (명칭만 수정하는 것으로 이해했는데 officeDTO로 바꾸는 걸 말씀하신거였으면 수정하겠습니다)
            val result = officeList.entries.mapNotNull { room ->
                getAvailableRoomInfo(room, capacity, needWindow, needFilmBooth)
                //필터링 된 회의실들을 받아서 줄 단위로 연결함
            }.joinToString(separator = Strings.NEW_LINE)
        }*/
        val result = officeList.entries.mapNotNull { room ->
            getAvailableRoomInfo(room, capacity, needWindow, needFilmBooth)
            //필터링 된 회의실들을 받아서 줄 단위로 연결함
        }.joinToString(separator = Strings.NEW_LINE)
        // result와 capacity를 Pair로 반환
        return Pair(result, capacity)
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process 중 조건에 맞는 회의실 필터링
     */
    private fun getAvailableRoomInfo(
        room: officeList,
        capacity: Int,
        needWindow: Boolean,
        needFilmBooth: Boolean
    ): String? {
        //window와 filmBooth가 false여도 true에 해당하는 회의실을 반환함과 동시에
        // window나 filmBooth가 true인 경우에는 true인 것만 반환하도록 하는 조건식
        if (capacity <= room.maxCapacity && (!needWindow || room.hasWindow) && (!needFilmBooth || room.hasFilmBooth)) {
            val windowInfo = if (room.hasWindow) Strings.YES_MESSAGE else Strings.NO_MESSAGE
            val filmBoothInfo = if (room.hasFilmBooth) Strings.YES_MESSAGE else Strings.NO_MESSAGE

            return String.format(
                Strings.STEP_1_1_ROOM_INFO,
                room.roomNumber,
                room.roomName,
                room.baseCapacity,
                room.maxCapacity,
                room.baseCostPerHour,
                room.additionalCostPerPerson,
                windowInfo,
                filmBoothInfo
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
        val phoneNumber = Input.isString()
        //TODO: 휴대폰 번호로 예약 내역 조회
    }

    /**
     * 메인 > [2]매출 관리
     *
     * desc: 매출 관리 process
     */
    private fun manageSalesProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_2_MENU_MESSAGE)

            when(Input.isInt()) {
                1 -> {}
                2 -> {}
                3 -> {}
                4 -> {}
                0 -> break
            }
        }
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
}