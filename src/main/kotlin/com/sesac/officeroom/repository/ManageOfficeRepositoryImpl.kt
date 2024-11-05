package com.sesac.officeroom.repository

import com.sesac.officeroom.data.dto.OfficeDTO
import com.sesac.officeroom.data.dto.ReservationDTO
import com.sesac.officeroom.data.source.OfficeDataSource
import com.sesac.officeroom.data.source.ReservationsDataSource
import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
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

    override suspend fun cancelReservation(phoneNumber: String, userReservations: List<ReservationDTO>) {
        val file = reservationsDataSource.readReservationsTxt()
        val reservations = file.readLines()

        if (userReservations.isEmpty()) {
            println(Strings.STEP_1_3_NO_NUMBER_FOUND)
            return
        }

        // 사용자 예약을 인덱스와 함께 표시
        println(Strings.STEP_1_3_NUMBER_FOUND)
        userReservations.forEachIndexed { index, reservation ->
            println("${index + 1}. 사무실 ID: ${reservation.officeId}, 날짜: ${reservation.date}, 시간: ${reservation.reservationTime}, 인원수: ${reservation.numberOfPeople}")
        }

        // 취소할 예약 번호 입력 받기
        val selectedIndex = Input.isInt() - 1

        if (selectedIndex < 0 || selectedIndex >= userReservations.size) {
            println(Strings.ERROR_MESSAGE)
            return
        }

        // 선택된 예약 제거
        val selectedReservation = userReservations[selectedIndex]
        val filteredReservations = reservations.filterNot { line ->
            val fields = line.split(",")
            fields[0].toInt() == selectedReservation.officeId &&
                    LocalDate.parse(fields[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")) == selectedReservation.date &&
                    LocalTime.parse(fields[2], DateTimeFormatter.ofPattern("HH:mm")) == selectedReservation.reservationTime &&
                    fields[3].toInt() == selectedReservation.usageTime &&
                    fields[4].toInt() == selectedReservation.numberOfPeople &&
                    fields[5].trim() == selectedReservation.phoneNumber
        }
        // 업데이트된 예약 내용을 파일에 다시 쓰기
        file.writeText(filteredReservations.joinToString("\n"))
        println(Strings.STEP_1_3_CANCEL_RESERVATION)
    }



//    override suspend fun cancelReservation(phoneNumber: String) {
//        val file = reservationsDataSource.readReservationsTxt()
//        val reservations = file.readLines()
//        val filteredReservations = reservations.filterNot { line ->
//            val fields = line.split(",")
//
//            // 전화번호는 6번째 필드
//            fields[5].trim() == phoneNumber
//        }
//
//        // 필터링된 예약 정보로 파일 덮어쓰기
//        file.writeText(filteredReservations.joinToString("\n"))
//    }
}