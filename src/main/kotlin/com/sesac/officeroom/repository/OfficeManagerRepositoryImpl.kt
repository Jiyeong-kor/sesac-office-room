package com.sesac.officeroom.repository

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.data.toCSVString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

class OfficeManagerRepositoryImpl(): OfficeManagerRepository {

    /**
     * 사무실 목록 불러오기
     */
    override suspend fun getOfficeList(): List<OfficeDTO> {
        val officeRooms = mutableListOf<OfficeDTO>()
        val file = File("Office.txt")

        file.forEachLine { line ->
            val data = line.split(",")
            if (data.size == 8) {
                val officeDTO = OfficeDTO(
                    id = data[0].toInt(),
                    name = data[1],
                    baseCapacity = data[2].toInt(),
                    maxCapacity = data[3].toInt(),
                    baseCostPerHour = data[4].toInt(),
                    additionalCostPerPerson = data[5].toInt(),
                    hasWindow = data[6].toBoolean(),
                    hasFilmBooth = data[7].toBoolean()
                )
                officeRooms.add(officeDTO)
            }
        }

        return officeRooms
    }

    /**
     * 예약하기: 예약 목록 추가
     */
    override suspend fun makeReservation(reservationDTO: ReservationDTO) {
        val file = File("Reservation.txt")
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
        }

        withContext(Dispatchers.IO) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = fileWriter.buffered()

            with(bufferedWriter) {
                write("${reservationDTO.toCSVString()}\n")
                flush()
                close()
            }
        }
    }
}