package com.sesac.officeroom.presentation.viewmodel

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
import com.sesac.officeroom.presentation.common.View
import com.sesac.officeroom.repository.ManageOfficeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class ManageOfficeViewModel(
    private val manageOfficeRepository: ManageOfficeRepository
) {

    // boolean 값을 string 값으로 변환
    val booleanToString: (bool: Boolean) -> String = { bool -> if(bool) Strings.YES else Strings.NO  }

    /**
     * 사무실 목록 불러오기
     */
    suspend fun getOfficeList() : List<OfficeDTO> {
        return withContext(Dispatchers.IO) {
            manageOfficeRepository.getOfficeList()
        }
    }

    /**
     * 예약 불러오기
     */
    suspend fun getReservationList(): List<ReservationDTO> {
        return withContext(Dispatchers.IO) {
            manageOfficeRepository.getReservationList()
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
    fun getAvailableTimesForDate(officeId: Int, date: LocalDate): List<Int> {

        //기존 예약 목록에서 특정 회의실 ID와 날짜에 해당하는 목록을 가져옴
        val reservations = runBlocking{
            getReservationList().filter { it.officeId == officeId && it.date == date }
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
            if (canMakeReservation(newReservation, reservations))
                availableTimes.add(hour)
        }
        return availableTimes
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 process 중 데이터를 저장하는 기능
     * writer: 정지영
     */
    suspend fun reserveDataOfRoom(
        year: Int, month: Int, dayOfMonth: Int, hour: Int,
        officeId: Int, usageTime: Int, numberOfPeople: Int, phoneNumber: String
    ): Boolean {
        val date = LocalDate.of(year, month, dayOfMonth)
        val time = LocalTime.of(hour, 0)
        val reservationDTO = ReservationDTO(
            officeId,
            date,
            time,
            usageTime,
            numberOfPeople,
            phoneNumber
        )

        //예약 생성
        return manageOfficeRepository.makeReservation(reservationDTO)
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 조건에 맞는 회의실이 존재하는 지를 Boolean 값으로 알려줌
     * writer: 정지영
     */
    fun doesTheRoomExist(availableRoomResult: String): Boolean {
        if (availableRoomResult.isEmpty()) {

            // 조건에 해당하는 회의실이 없을 경우 보여줄 메시지
            View.prettyPrintConsole(Strings.STEP_1_1_NO_ROOM_FOUND)
            return false
        } else {

            //예약을 원하는 회의실 번호를 입력하라는 메시지
            View.prettyPrintConsole(Strings.STEP_1_1_ROOM_CHOOSE)
            println(availableRoomResult)
            return true
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [1]회의실 예약
     *
     * desc: 회의실 예약 과정 중 조건에 해당하여 예약이 가능한 회의실을 보여주는 기능
     * writer: 정지영
     */
    fun showAvailableRooms(): Pair<String, Int> {
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
            getOfficeList()
        }

        //필터링 된 회의실들을 받아서 줄 단위로 연결함
        val result = officeList.mapNotNull { room ->
            getAvailableRoomInfo(room, capacity, needWindow, needFilmBooth)
        }.joinToString(separator = Strings.NEW_LINE)
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
        /*
        window와 filmBooth가 false여도 true에 해당하는 회의실을 반환함과 동시에
        window나 filmBooth가 true인 경우에는 true인 것만 반환하도록 하는 조건식
         */
        with(room) {
            if (capacity <= maxCapacity && (!needWindow || hasWindow) && (!needFilmBooth || hasFilmBooth)) {
                return getOfficeItemToString(this)
            }
        }
        return null
    }

    /**
     * 메인 > [1]회의실 관리 > [2]회의실 정보 확인 > [2]회의실 별 예약현황 조회
     *
     * desc: 회의실 별 예약 목록 불러오기
     * writer: 박혜선
     */
    suspend fun getReservationStatusByOffice(officeId: Int): List<ReservationDTO> {
        return manageOfficeRepository.getReservationList().filter { reservation ->
            reservation.officeId == officeId
        }
    }

    /**
     * 새로운 예약의 예약시간과 기존의 예약 시간에 충돌이 있는지 확인하는 함수
     * writer: 전지환
     */
    private fun canMakeReservation(newReservation: ReservationDTO, oldReservations: List<ReservationDTO>): Boolean {
        /*
        newReservation -> 새롭게 생성하고자 하는 예약
        oldReservations -> 예약 목록, 즉 기존의 예약들
        모든 예약을 돌음
        */
        for (reservation in oldReservations) {
            if ((reservation.date == newReservation.date) && (reservation.officeId == newReservation.officeId)) {
                //예약 날짜와 예약 회의실이 모두 같은 경우에만

                val oldStart = reservation.reservationTime
                //oldStart: 기존 예약의 시작시간
                val oldEnd = reservation.reservationTime.plusHours(reservation.usageTime.toLong())
                //oldEnd: 시작시간에 usageTime을 더하면 기존 예약의 종료시간
                val newStart = newReservation.reservationTime
                //newStart: 새 예약의 시작시간
                val newEnd = newReservation.reservationTime.plusHours(newReservation.usageTime.toLong())
                //newEnd: 새 예약의 종료시간

                if (!(newEnd.isBefore(oldStart) || newStart.isAfter(oldEnd))) {
                    /*
                    첫번째 조건은 새 예약이 끝나는 시간이 기존 예약의 시작 시간 이전인 경우 무조건 예약이 가능함
                    두번째 조건은 새 예약이 시작되는 시간이 기존 예약의 종료 시간보다 이후인 경우 무조건 예약이 가능함
                    두 조건의이 하나라도 걸리면 무조건 예약이 가능하므로, 이 두 조건을 OR 한 것의 NOT을 하면 예약이 겹치는 경우가 됨,
                    따라서 이 때 false
                    */
                    return false
                }
            }
        }
        //반복문을 돌면서 조건에 아무것도 걸리지 않았으므로 true가 리턴됨
        return true
    }

    /**
     * Office item을 String 형식으로 변환
     * 형식: (id. name: 기준 인원 n명, 최대 인원 n명, 기본 요금 n원, 인당 추가 요금 n원, 창문 있음/없음, 포토부스 있음/없음)
     *
     * writer: 박혜선
     */
    fun getOfficeItemToString(officeItem: OfficeDTO): String {
        return with(officeItem) {
            String.format(Strings.STEP_1_1_ROOM_INFO,
                id, name, baseCapacity, maxCapacity, baseCostPerHour, additionalCostPerPerson,
                booleanToString(hasWindow), booleanToString(hasFilmBooth))
        }
    }
    /**
     * 핸드폰 번호로 예약을 취소함
     *
     * writer: 정지영
     */
    suspend fun cancelReservation(phoneNumber: String) {
        manageOfficeRepository.cancelReservation(phoneNumber)
    }
}
