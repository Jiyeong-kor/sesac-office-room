package com.sesac.officeroom.repository

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO

interface OfficeManagerRepository {
    suspend fun getOfficeList(): List<OfficeDTO>
    suspend fun makeReservation(reservationDTO: ReservationDTO)
    suspend fun getReservationList():List<ReservationDTO>
}