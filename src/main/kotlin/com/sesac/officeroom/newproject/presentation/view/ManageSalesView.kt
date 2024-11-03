package com.sesac.officeroom.newproject.presentation.view

import com.sesac.officeroom.newproject.presentation.viewmodel.ManageOfficeViewModel
import com.sesac.officeroom.newproject.presentation.viewmodel.SalesCalculator
import com.sesac.officeroom.newproject.repository.ManageOfficeRepositoryImpl
import com.sesac.officeroom.newproject.presentation.common.Input
import com.sesac.officeroom.newproject.presentation.common.Strings
import com.sesac.officeroom.newproject.presentation.common.View
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 메인 > [2]매출 관리
 *
 * desc: 매출 관리 process
 */
class ManageSalesView {
    /**
     * 매출 관리 main 함수
     * getReservationList()가 suspend 함수이므로 죄다 suspend로 바뀜
     * writer:전지환
     */
    suspend fun main() {
        val viewModel = ManageOfficeViewModel(ManageOfficeRepositoryImpl())
        val reservations = viewModel.getReservationList()
        val officeList = viewModel.getOfficeList()
        while (true) {
            View.prettyPrintConsole(Strings.STEP_2_MENU_MESSAGE)
            when (Input.isInt()) {
                1 -> { // 총 매출 조회
                    val totalSales = SalesCalculator.calculateTotalSales(reservations, officeList) //SalesCalculator:계산 기능만 가진 오브젝트
                    View.prettyPrintConsole(String.format(Strings.STEP_2_1_MESSAGE, totalSales))
                }
                2 -> { //회의실 별 매출 조회
                    val officeListMessage = officeList.joinToString(prefix = Strings.ROOMS_INDEX, separator = Strings.NEW_LINE) {
                        "${officeList.indexOf(it) + 1}. ${it.name}" } //모든 office를 돌면서 단순 officeListMessage 문자열에 개행으로 추가함, index는 0부터 시작하므로 indexOf(it)에 +1을 해서 문자열에 추가
                    View.prettyPrintConsole(officeListMessage)
                    val officeSelect = Input.isInt() //보고싶은 회의실 입력받음
                    if (officeSelect in 1..officeList.size) {
                        val officeId = officeList[officeSelect- 1].id
                        val officeSales = SalesCalculator.calculateSalesByOffice(reservations, officeList, officeId)
                        View.prettyPrintConsole(String.format(Strings.STEP_2_2_MESSAGE,officeList[officeId-1].name,officeSales))
                    } else {
                        View.prettyPrintConsole(Strings.ERROR_MESSAGE)
                    }
                }
                3 -> { //날짜별 매출조회
                    View.prettyPrintConsole(Strings.STEP_2_3_MESSAGE_1)
                    val inputDate = Input.isString() //문자열로 입력받음
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val date = try {
                        LocalDate.parse(inputDate, formatter) //제대로 된 문자열 형식이 들어왔는지 확인하고, 아니면 null 내보냄
                    } catch (e: Exception) {
                        null
                    }
                    if (date != null) { //제대로 입력받은 경우
                        val dateSales = SalesCalculator.calculateSalesByDate(reservations, officeList, date)
                        if (dateSales > 0) {
                            View.prettyPrintConsole(String.format(Strings.STEP_2_3_MESSAGE_2,date,date,date,dateSales))
                            View.prettyPrintConsole("$date 매출: ${dateSales}원")
                        } else {
                            View.prettyPrintConsole(String.format(Strings.STEP_2_3_MESSAGE_3,date,date,date))
                            View.prettyPrintConsole("${date}에 예약이 없습니다.")
                        }
                    } else {
                        View.prettyPrintConsole(Strings.STEP_2_3_ERROR) //이상한 날짜 형식 입력
                    }
                }
                4 -> { //총 인원 조회
                    val totalUsers = reservations.sumOf { it.numberOfPeople }
                    View.prettyPrintConsole(String.format(Strings.STEP_2_4_MESSAGE,totalUsers))
                }
                0 -> break
            }
        }
    }
}