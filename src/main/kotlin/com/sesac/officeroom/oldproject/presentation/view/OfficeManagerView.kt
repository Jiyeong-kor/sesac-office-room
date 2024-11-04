package com.sesac.officeroom.presentation.view

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
import com.sesac.officeroom.presentation.common.View
import com.sesac.officeroom.presentation.viewmodel.OfficeManagerViewModel
import com.sesac.officeroom.repository.OfficeManagerRepositoryImpl
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
                -2 -> test2()
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
     * writer: 정지영
     */
    private fun roomReservationProcess() {
        //조건을 입력받고 예약이 가능한 회의실을 보여줌
        val (availableRoomResult, numberOfPeople) = showAvailableRooms()
        //예약이 가능한 회의실이 존재하는지를 확인
        if(doesTheRoomExist(availableRoomResult)) {
            //존재한다면 회의실 번호를 입력 받음
            //TODO: id가 있는 회의실 값만 입력받고 없는 경우 예외 처리
            val officeId = Input.isInt()
            View.prettyPrintConsole(Strings.STEP_1_1_USAGE_DATE_CHOOSE)
            //오늘을 포함해 7일치 예약 가능 날짜를 보여주고 날짜의 인덱스로 날짜를 입력받음
            val availableDates = View.showDates()
            val dayIndex = Input.isInt()
            //인덱스로 입력받은 날짜를 날짜를 사용할 수 있도록 바꿈
            val chosenDate = availableDates[dayIndex -1]
            //해당하는 회의실의 예약 가능한 시간대 표시
            val availableTimes = getAvailableTimesForDate(officeId, chosenDate)
            //예약이 불가능한 경우
            if(availableTimes.isEmpty()) return View.prettyPrintConsole(Strings.STEP_1_1_NO_ROOM_FOUND)
            //예약이 가능한 경우
            View.prettyPrintConsole(String.format(Strings.STEP_1_1_AVAILABLE_TIMES_MESSAGE, availableTimes.joinToString(", ")))
            //이용 시작 시간 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_1_START_TIME_CHOOSE)
            val startTime = Input.isInt()
            //이용 시간 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_1_USAGE_TIME_CHOOSE)
            val usageTime = Input.isInt()
            //휴대폰 번호 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
            val phoneNumber = Input.isString()
            //예약 정보 저장
            reserveDataOfRoom(
                chosenDate.year, chosenDate.monthValue, chosenDate.dayOfMonth, startTime, officeId, usageTime, numberOfPeople, phoneNumber
            )
        }
    }
    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * @param officeId 예약하려는 회의실의 ID
     * @param date 예약을 원하는 날짜
     * @return 해당 날짜에 예약 가능한 시간 리스트 (09:00 ~ 18:00)
     *
     * desc: 회의실 예약 process 중 지정된 날짜에 대해 예약 가능한 시간대를 반환
     * writer: 정지영
     */
    private fun getAvailableTimesForDate(officeId: Int, date: LocalDate): List<Int> {

            //기존 예약 목록에서 특정 회의실 ID와 날짜에 해당하는 목록을 가져옴
            val reservations = runBlocking{
                viewModel.getReservationList().filter { it.officeId == officeId && it.date == date }
            }
            //예약 가능한 시간을 저장할 리스트 생성
            val availableTimes = mutableListOf<Int>()

        //9시부터 18시까지 새로운 예약 객체 생성
        for (hour in 9..17) {
            val newReservation = ReservationDTO(
                officeId,
                date,
                //임의 값으로 생성
                LocalTime.of(hour, 0),
                1,
                1,
                ""
            )
            //예약이 가능한지 확인하고 가능하면 해당 시간을 availableTimes에 추가
            //TODO: 예약 과정에서 예약 불가능한 시간대에 예약이 되는 오류 수정
            if (viewModel.canMakeReservation(newReservation, reservations))
                availableTimes.add(hour)
        }
        //예약 가능한 시간 리스트 return
        return availableTimes
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process 중 데이터를 저장하는 기능
     * writer: 지영
     */
    private fun reserveDataOfRoom(year: Int, month: Int, dayOfMonth: Int, hour: Int, officeId: Int, usageTime: Int, numberOfPeople: Int, phoneNumber: String) {
        runBlocking {
            val date = LocalDate.of(year, month, dayOfMonth)
            val time = LocalTime.of(hour, 0)
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
     * writer: 지영
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
     * writer: 정지영
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
        val officeList = runBlocking{
            viewModel.getOfficeList()
        }
        val result = officeList.mapNotNull { room ->
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
     * writer: 정지영
     */
    private fun getAvailableRoomInfo(
        room: OfficeDTO,
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
                room.id,
                room.name,
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
     * desc: 회의실 예약 내역 process
     * writer: 정지영
     */
    private fun reservationInfoProcess() {
        //핸드폰 번호 입력
        View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
        val phoneNumber = Input.isString()
        //예약 내역 가져오기
        val reservations = runBlocking{
            viewModel.getReservationList()
        }
        val userReservations = reservations.filter { it.phoneNumber ==phoneNumber }
        //예약 내역이 없으면
        if (userReservations.isEmpty()) {
            View.prettyPrintConsole(Strings.STEP_1_3_NO_NUMBER_FOUND)
        } else {
            //예약 내역이 있으면
            View.prettyPrintConsole(Strings.STEP_1_3_NUMBER_FOUND)
            userReservations.forEach { reservation ->
                val formattedMessage = String.format(
                    Strings.STEP_1_3_RESERVATION_INFO_FORMAT,
                    reservation.officeId,
                    reservation.date,
                    reservation.reservationTime,
                    reservation.usageTime,
                    reservation.numberOfPeople
                )
                println(formattedMessage)
            }
        }
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

    private fun test() {
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
    /**
     * writer: 전지환
     */
    private fun test2(){
        //Reservations.txt에 제대로 데이터가 파싱되었는지 확인하는 함수
        runBlocking {
            val reservationList = viewModel.getReservationList()
            reservationList.listIterator().forEach {
                println(it)
            }
        }
    }
}