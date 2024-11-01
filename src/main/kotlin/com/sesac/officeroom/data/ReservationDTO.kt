package com.sesac.officeroom.data

import java.time.LocalDate
import java.time.LocalTime

/**
 * ReservationDTO
 *
 * @property officeId 예약한 사무실 id
 * @property date 예약 날짜
 * @property reservationTime 예약시간
 * @property usageTime 이용시간
 * @property numberOfPeople 인원수
 * @property phoneNumber 예약자 휴대폰번호
 */
data class ReservationDTO(
    val officeId: Int,
    val date: LocalDate,
    val reservationTime: LocalTime,
    val usageTime: Int,
    val numberOfPeople: Int,
    val phoneNumber: String,
)

fun ReservationDTO.toCSVString(): String {
    return listOf(
        officeId,
        date,
        reservationTime,
        usageTime,
        numberOfPeople,
        phoneNumber
    ).joinToString(",")
}