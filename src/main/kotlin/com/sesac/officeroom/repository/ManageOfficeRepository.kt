package com.sesac.officeroom.repository

import com.sesac.officeroom.data.dto.OfficeDTO
import com.sesac.officeroom.data.dto.ReservationDTO

interface ManageOfficeRepository {
    suspend fun getOfficeList(): List<OfficeDTO>
    suspend fun makeReservation(reservationDTO: ReservationDTO): Boolean
    suspend fun getReservationList():List<ReservationDTO>
    suspend fun cancelReservation(phoneNumber: String, userReservations: List<ReservationDTO>)
}