package com.sesac.officeroom.presentation.view

import com.sesac.officeroom.data.dto.OfficeDTO
import com.sesac.officeroom.data.dto.ReservationDTO
import com.sesac.officeroom.data.source.OfficeDataSourceImpl
import com.sesac.officeroom.data.source.ReservationsDataSourceImpl
import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
import com.sesac.officeroom.presentation.common.View
import com.sesac.officeroom.presentation.viewmodel.ManageOfficeViewModel
import com.sesac.officeroom.repository.ManageOfficeRepositoryImpl
import kotlinx.coroutines.runBlocking

/**
 * 메인 > [1]회의실 관리
 *
 * desc: 회의실 관리 process
 */
class ManageOfficeView {
    private val viewModel = ManageOfficeViewModel(
        ManageOfficeRepositoryImpl(
            OfficeDataSourceImpl(), ReservationsDataSourceImpl()
        ))

    fun main() {
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
     * writer: 정지영
     */
    private fun roomReservationProcess() {

        //조건을 입력받고 예약이 가능한 회의실을 보여줌
        val (availableRoomResult, numberOfPeople) = viewModel.showAvailableRooms()

        //예약이 가능한 회의실이 존재하는지를 확인
        if (viewModel.doesTheRoomExist(availableRoomResult)) {

            //존재한다면 회의실 번호를 입력 받음
            //TODO: id가 있는 회의실 값만 입력받고 없는 경우 예외 처리
            val officeId = Input.isInt()
            View.prettyPrintConsole(Strings.STEP_1_1_USAGE_DATE_CHOOSE)

            //오늘을 포함해 7일치 예약 가능 날짜를 보여주고 날짜의 인덱스로 날짜를 입력받음
            val availableDates = View.showDates()
            val dayIndex = Input.isInt()

            //인덱스로 입력받은 날짜를 날짜를 사용할 수 있도록 바꿈
            val chosenDate = availableDates[dayIndex - 1]

            //해당하는 회의실의 예약 가능한 시간대 표시
            val availableTimes = viewModel.getAvailableTimesForDate(officeId, chosenDate)

            //예약이 불가능한 경우
            if (availableTimes.isEmpty()) return View.prettyPrintConsole(Strings.STEP_1_1_NO_ROOM_FOUND)

            //예약이 가능한 경우
            View.prettyPrintConsole(
                String.format(
                    Strings.STEP_1_1_AVAILABLE_TIMES_MESSAGE,
                    availableTimes.joinToString(", ")
                )
            )

            //이용 시작 시간 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_1_START_TIME_CHOOSE)
            val startTime = Input.isInt()

            //이용 시간 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_1_USAGE_TIME_CHOOSE)
            val usageTime = Input.isInt()

            // 겹치는 시간대가 있는지 확인
            val isAvailableTime = runBlocking {
                viewModel.checkAvailableTime(officeId, chosenDate, startTime, usageTime)
            }
            if(!isAvailableTime)
                return View.prettyPrintConsole("예약이 불가능한 날짜입니다.")

            //휴대폰 번호 입력 받기
            View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
            val phoneNumber = Input.isString()

            runBlocking {
                //예약 정보 저장
                val saveResult = viewModel.reserveDataOfRoom(
                    chosenDate.year, chosenDate.monthValue, chosenDate.dayOfMonth, startTime,
                    officeId, usageTime, numberOfPeople, phoneNumber
                )

                View.prettyPrintConsole(
                    when(saveResult) {
                        true -> "예약 성공"
                        false -> "예약 실패"
                    }
                )
            }
        }
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
                1 -> getOfficeList()
                2 -> getReservationStatus()
                0 -> break
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [2]회의실 정보 확인 > [1]회의실 목록 조회
     *
     * desc: 회의실 목록 조회
     * writer: 박혜선
     */
    private fun getOfficeList() {
        runBlocking {
            val officeList = viewModel.getOfficeList()
            officeList.listIterator().forEach { officeItem ->
                println(viewModel.getOfficeItemToString(officeItem))
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [2]회의실 정보 확인 > [2]회의실 별 예약현황 조회
     *
     * desc: 회의실 별 예약현황 조회
     * writer: 박혜선
     */
    private fun getReservationStatus() {
        runBlocking {
            // 회의실 목록 안내
            val officeList = viewModel.getOfficeList()
            View.prettyPrintConsole(getOfficeListMessage(officeList))

            val officeId = Input.isInt()
            val reservationList = viewModel.getReservationStatusByOffice(officeId)

            when (reservationList.size) {
                0 -> View.prettyPrintConsole(Strings.STEP_1_2_2_NO_RESERVATION_MESSAGE)
                else -> View.createSchedule(reservationList)
            }
        }
    }

    /**
     * 회의실 목록 조회 (형식: id.name)
     *
     * desc: 회의실 목록을 양식에 맞게 변환하여 String 값으로 return 하는 함수
     * writer: 박혜선
     */
    private fun getOfficeListMessage(officeList: List<OfficeDTO>): String {
        return officeList.joinToString(prefix = Strings.STEP_1_2_2_TITLE_MESSAGE, separator = Strings.NEW_LINE) {
            String.format(Strings.ROOMS_NAME, officeList.indexOf(it) + 1, it.name)
        }
    }
    
    /**
     * 메인 > [1]회의실 관리 > [3]회의실 예약내역 조회
     *
     * desc: 회의실 예약 내역 조회 process
     * writer: 정지영
     */
    private fun reservationInfoProcess() {

        //핸드폰 번호 입력
        View.prettyPrintConsole(Strings.STEP_1_3_MESSAGE)
        val phoneNumber = Input.isString()

        //예약 내역 가져오기
        val reservations = runBlocking {
            viewModel.getReservationList()
        }

        val userReservations = reservations.filter { it.phoneNumber == phoneNumber }

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

            while (true) {
                View.prettyPrintConsole(Strings.STEP_1_3_MENU_MESSAGE)

                when (Input.isInt()) {
                    1 -> cancelReservation(phoneNumber, userReservations)
                    2 -> {} //공유하기
                    0 -> break
                }
            }
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [3]회의실 예약내역 조회 > [1] 예약취소
     *
     * desc: 회의실 예약 내역 후 취소 process
     * writer: 정지영
     */
    private fun cancelReservation(phoneNumber: String, userReservations:List<ReservationDTO>) {
        runBlocking {
            viewModel.cancelReservation(phoneNumber, userReservations)
            View.prettyPrintConsole(Strings.STEP_1_3_CANCEL_RESERVATION)
        }
    }
}