package com.sesac.officeroom.util

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import java.time.LocalDate

/**
 * 매출 계산기
 * writer:전지환
 */
object SalesCalculator {
    fun calculateJustCost(reservation:ReservationDTO, officeList: List<OfficeDTO>):Int{ //예약과 회의실 목록을 받아,
        //회의실의 데이터를 토대로 금액을 계산하는 함수
        val office = officeList.first{it.id ==reservation.officeId } //id에 중복은 없으므로 first를 사용하여 예약된 회의실 id를 찾음
        val baseCost = office.baseCostPerHour * reservation.usageTime //기본요금
        val additionalCost = if(reservation.numberOfPeople > office.baseCapacity){ //기준인원보다 많은 인원이 존재하는 경우에만 추가금액 계산
            (reservation.numberOfPeople - office.baseCapacity) * office.additionalCostPerPerson
        } else {0}
        return baseCost + additionalCost //기본요금에 추가요금 더해서 리턴
    }
    fun calculateTotalSales(reservations : List<ReservationDTO>, officeList: List<OfficeDTO>) : Int {
        return reservations.sumOf{ calculateJustCost(it, officeList) } //총 매출을 반환하는 함수
    }
    fun calculateSalesByOffice(reservations:List<ReservationDTO>, officeList: List<OfficeDTO>, officeId:Int) : Int{
        return reservations.filter{ it.officeId == officeId }
            .sumOf { calculateJustCost(it,officeList) } // 회의실 id를 같이 받아서 그 회의실 매출만 계산하는 함수
    }
    fun calculateSalesByDate(reservations: List<ReservationDTO>, officeList: List<OfficeDTO>, date: LocalDate):Int{
        return reservations.filter { it.date == date }
            .sumOf { calculateJustCost(it,officeList) } //특정 날짜를 받아서 그 날짜의 매출만 계산하여 반환하는 함수
    }
}