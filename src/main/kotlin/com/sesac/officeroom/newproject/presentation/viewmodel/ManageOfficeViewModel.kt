package com.sesac.officeroom.newproject.presentation.viewmodel

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.newproject.repository.ManageOfficeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ManageOfficeViewModel(
    private val manageOfficeRepository: ManageOfficeRepository
) {
    /**
     * 사무실 목록 불러오기
     */
    suspend fun getOfficeList() : List<OfficeDTO> {
        return withContext(Dispatchers.IO) {
            manageOfficeRepository.getOfficeList()
        }
    }

    /**
     * 예약 생성하기
     */
    suspend fun makeReservation(reservationDTO: ReservationDTO) {
        manageOfficeRepository.makeReservation(reservationDTO)
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
     * 새로운 예약의 예약시간과 기존의 예약 시간에 충돌이 있는지 확인하는 함수
     * writer: 전지환
     */
    fun canMakeReservation(newReservation: ReservationDTO, oldReservations: List<ReservationDTO>): Boolean {
        //newReservation -> 새롭게 생성하고자 하는 예약
        //oldReservations -> 예약 목록, 즉 기존의 예약들
        for (reservation in oldReservations) { //모든 예약을 돌음
            if ((reservation.date == newReservation.date) && (reservation.officeId == newReservation.officeId)) { //예약 날짜와 예약 회의실이 모두 같은 경우에만
                val oldStart = reservation.reservationTime
                //oldStart: 기존 예약의 시작시간
                val oldEnd = reservation.reservationTime.plusHours(reservation.usageTime.toLong())
                //oldEnd: 시작시간에 usageTime을 더하면 기존 예약의 종료시간
                val newStart = newReservation.reservationTime
                //newStart: 새 예약의 시작시간
                val newEnd = newReservation.reservationTime.plusHours(newReservation.usageTime.toLong())
                //newEnd: 새 예약의 종료시간
                if (!(newEnd.isBefore(oldStart) || newStart.isAfter(oldEnd))) {
                    //첫번째 조건은 새 예약이 끝나는 시간이 기존 예약의 시작 시간 이전인 경우 무조건 예약이 가능함
                    //두번째 조건은 새 예약이 시작되는 시간이 기존 예약의 종료 시간보다 이후인 경우 무조건 예약이 가능함
                    //두 조건의이 하나라도 걸리면 무조건 예약이 가능하므로, 이 두 조건을 OR 한 것의 NOT을 하면 예약이 겹치는 경우가 됨,
                    // 따라서 이 때 false
                    return false
                }
            }
        }
        return true //반복문을 돌면서 조건에 아무것도 걸리지 않았으므로 true가 리턴됨
    }

}