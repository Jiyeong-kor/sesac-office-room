package com.sesac.officeroom.repository

import com.sesac.officeroom.data.OfficeDTO
import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.data.source.OfficeDataSource
import com.sesac.officeroom.data.source.ReservationsDataSource
import com.sesac.officeroom.presentation.common.Strings
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ManageOfficeRepositoryImpl(
    private val officeDataSource: OfficeDataSource,
    private val reservationsDataSource: ReservationsDataSource,
): ManageOfficeRepository {

    /**
     * 사무실 목록 불러오기
     */
    override suspend fun getOfficeList(): List<OfficeDTO> {
        val officeRooms = mutableListOf<OfficeDTO>()

        var file = officeDataSource.readOfficeTxt()

        return try {
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
            officeRooms
        } catch (e: FileNotFoundException) {
            println(Strings.EMPTY_OFFICE_FILE)
            emptyList()
        }
    }

    /**
     * 예약하기: 예약 목록 추가
     */
    override suspend fun makeReservation(reservationDTO: ReservationDTO): Boolean {
        return reservationsDataSource.saveReservationsTxt(reservationDTO)
    }

    /**
     * 예약 목록 불러오기
     */
    override suspend fun getReservationList(): List<ReservationDTO> {
        val reservationData = mutableListOf<ReservationDTO>()
        val file = reservationsDataSource.readReservationsTxt()
        return try {
            file.forEachLine { line ->
                val data = line.split(",")
                if (data.size == 6) {
                    val reservationDataDTO = ReservationDTO(
                        officeId = data[0].toInt(),

                        //date가 LocalDate 타입이므로, 스트링으로 저장된 data[1]을 파싱함
                        date = LocalDate.parse(data[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")),

                        //마찬가지로 reservationTime은 LocalTime 타입임
                        reservationTime = LocalTime.parse(data[2], DateTimeFormatter.ofPattern("HH:mm")),

                        usageTime = data[3].toInt(),
                        numberOfPeople = data[4].toInt(),
                        phoneNumber = data[5]
                    )
                    reservationData.add(reservationDataDTO)
                }
            }
            reservationData
        } catch (e: FileNotFoundException) {
            println(Strings.EMPTY_RESERVATIONS_FILE)
            emptyList()
        }
    }

    /**
     * 메인 > [1]회의실 관리 > [3]회의실 예약내역 조회 > [1] 예약취소
     *
     * desc: 회의실 예약 내역 조회 후 취소 process
     * writer: 정지영
     */
    override suspend fun cancelReservation(phoneNumber: String) {
        val reservationFile = File("Reservations.txt")
        val reservations = reservationFile.readLines()
        val filteredReservations = reservations.filterNot { line ->
            val fields = line.split(",")

            // 전화번호는 6번째 필드
            fields[5].trim() == phoneNumber
        }

        // 필터링된 예약 정보로 파일 덮어쓰기
        reservationFile.writeText(filteredReservations.joinToString("\n"))
    }
}