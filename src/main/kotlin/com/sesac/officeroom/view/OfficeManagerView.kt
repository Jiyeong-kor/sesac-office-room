package com.sesac.officeroom.view

import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.repository.OfficeManagerRepositoryImpl
import com.sesac.officeroom.viewmodel.OfficeManagerViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date


class OfficeManagerView {
    val viewModel = OfficeManagerViewModel(OfficeManagerRepositoryImpl())

    /**
     * 메인 process
     */
    fun mainProcess() {
        while (true) {
            View.prettyPrintConsole(Strings.MAIN_MESSAGE)

            when(Input.isInt()) {
                1 -> manageOfficeRoomProcess()
                2 -> manageSalesProcess()
                3 -> {
                    runBlocking {
                        val officeList = viewModel.getOfficeList()
                        officeList.listIterator().forEach {
                            println(it.id)
                        }
                    }
                }
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
                1 -> {
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
                2 -> officeRoomInfoProcess()
                3 -> reservationInfoProcess()
                0 -> break
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
}