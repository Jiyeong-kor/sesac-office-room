package com.sesac.officeroom.newproject.presentation.viewmodel

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.newproject.presentation.common.Input
import com.sesac.officeroom.newproject.presentation.common.Strings
import com.sesac.officeroom.newproject.presentation.common.View
import com.sesac.officeroom.newproject.repository.ManageOfficeRepository
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * ManageSalesViewModel: ManageSalesView에 사용할 로직들이 존재하는 뷰모델
 * writer: 전지환
 */

class ManageSalesViewModel(private val repository:ManageOfficeRepository) {
    /**
     * office 정보를 가져오는 함수
     */
    suspend fun getOfficeList(): List<OfficeDTO> {
        return repository.getOfficeList()
    }

    /**
     * 예약 정보를 가져오는 함수
     */
    suspend fun getReservations(): List<ReservationDTO>{
        return repository.getReservationList()
    }

    /**
     * 회의실 목록을 가져와서 개행하여 문자열로 내보내는 함수
     */
    fun getOfficeListMessage(officeList: List<OfficeDTO>): String {
        return officeList.joinToString(prefix = Strings.ROOMS_INDEX, separator = Strings.NEW_LINE) {
            String.format(Strings.ROOMS_NAME, officeList.indexOf(it) + 1, it.name)
        }
    }

    /**
     * 총 매출 출력 함수
     */
    suspend fun displayTotalOfficesSales() {
        val reservations = getReservations()
        val officeList = getOfficeList()
        val totalSales = calculateTotalSales(reservations, officeList)
        View.prettyPrintConsole(String.format(Strings.STEP_2_1_MESSAGE, totalSales))
    }

    /**
     * 회의실 별 매출 출력 함수
     */
    suspend fun displaySalesByOffice() {
        val reservations = getReservations()
        val officeList = getOfficeList()
        val officeListMessage = getOfficeListMessage(officeList) //회의실 목록 출력
        View.prettyPrintConsole(officeListMessage)

        val officeSelect = Input.isInt()
        if (officeSelect in 1..officeList.size) { //제대로 된 범위 내에 입력했는지 확인
            val officeId = officeList[officeSelect - 1].id //index는 0부터 시작하므로 1 줄임
            val officeSales = calculateSalesByOffice(reservations, officeList, officeId)
            View.prettyPrintConsole(String.format(Strings.STEP_2_2_MESSAGE,officeList[officeSelect - 1].name,officeSales))
        } else {
            View.prettyPrintConsole(Strings.ERROR_MESSAGE)
        }
    }

    /**
     * 날짜 별 매출 출력 함수
     */
    suspend fun displaySalesByDate(){
        View.prettyPrintConsole(Strings.STEP_2_3_MESSAGE_1)
        val reservations = getReservations()
        val officeList = getOfficeList()
        val inputDate = Input.isString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") //입력받을 날짜 형식 지정
        val date = try{
            LocalDate.parse(inputDate, formatter)
        } catch (e:Exception) { //파싱 실패 시 오류발생 후 null 반환
            null
        }
        if (date!=null){
            val dateSales = calculateSalesByDate(reservations, officeList, date)
            if (dateSales > 0){
                View.prettyPrintConsole(String.format(Strings.STEP_2_3_MESSAGE_2,date,date,date,dateSales))
            }
            else{ //계산기에서 0이나왔다는 소리는 매출이 없음
                View.prettyPrintConsole(String.format(Strings.STEP_2_3_MESSAGE_3,date,date,date))
            }
        }
        else{
            View.prettyPrintConsole(Strings.STEP_2_3_ERROR)
        }
    }

    /**
     * 모든 회의실 사용자 수 출력 함수
     */
    suspend fun displayTotalUsers(){
        val reservations = getReservations()
        val totalUsers = reservations.sumOf { it.numberOfPeople }
        View.prettyPrintConsole(String.format(Strings.STEP_2_4_MESSAGE,totalUsers))
    }

    /**
     * 계산 관련 함수들
     * calculateJustCost: 예약과 회의실 목록을 받아, 회의실의 데이터를 토대로 금액을 계산하는 함수
     */
    fun calculateJustCost(reservation: ReservationDTO, officeList: List<OfficeDTO>): Int {
        val office = officeList.first { it.id == reservation.officeId } //id에 중복은 없으므로 first를 사용하여 예약된 회의실 id를 찾음
        val baseCost = office.baseCostPerHour * reservation.usageTime //기본요금
        val additionalCost = if (reservation.numberOfPeople > office.baseCapacity) { //기준인원보다 많은 인원이 존재하는 경우에만 추가금액 계산
            (reservation.numberOfPeople - office.baseCapacity) * office.additionalCostPerPerson
        } else {
            0
        }
        return baseCost + additionalCost //기본요금에 추가요금 더해서 리턴
    }

    /**
     * 총 매출 반환 함수
     */
    fun calculateTotalSales(reservations: List<ReservationDTO>, officeList: List<OfficeDTO>): Int {
        return reservations.sumOf { calculateJustCost(it, officeList) } //총 매출을 반환하는 함수
    }

    /**
     * 회의실 id를 같이 받아서 그 회의실 매출만 계산하는 함수
     */
    fun calculateSalesByOffice(reservations: List<ReservationDTO>, officeList: List<OfficeDTO>, officeId: Int): Int {
        return reservations.filter { it.officeId == officeId }
            .sumOf { calculateJustCost(it, officeList) }
    }

    /**
     * 특정 날짜를 받아 그 날짜의 매출만 계산하여 반환하는 함수
     */
    fun calculateSalesByDate(reservations: List<ReservationDTO>, officeList: List<OfficeDTO>, date: LocalDate): Int {
        return reservations.filter { it.date == date }
            .sumOf { calculateJustCost(it, officeList) }
    }
}


