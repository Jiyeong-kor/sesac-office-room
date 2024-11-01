package com.sesac.officeroom.viewmodel

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.repository.OfficeManagerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfficeManagerViewModel(
    private val officeManagerRepository: OfficeManagerRepository
) {

    /**
     * 사무실 목록 불러오기
     */
    suspend fun getOfficeList() : List<OfficeDTO> {
        return withContext(Dispatchers.IO) {
            officeManagerRepository.getOfficeList()
        }
    }

    /**
     * 예약 생성하기
     */
    suspend fun makeReservation(reservationDTO: ReservationDTO) {
        officeManagerRepository.makeReservation(reservationDTO)
    }
}