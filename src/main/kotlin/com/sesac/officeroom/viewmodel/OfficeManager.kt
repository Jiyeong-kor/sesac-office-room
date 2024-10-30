package com.sesac.officeroom.viewmodel

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

            when(Input.isInt()) {
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

            when(Input.isInt()) {
                1 -> {}
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