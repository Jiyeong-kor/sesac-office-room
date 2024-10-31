package com.sesac.officeroom.repository

import com.sesac.officeroom.data.MeetingRoom
import java.time.LocalDate

/**
 * 예약에 대한 데이터를 담는 클래스
 */
data class Reservation(
    val date: LocalDate,
    val phoneNumber: String,
    val availableTimes: MutableList<Boolean> = MutableList(9)
            {true} // 9시부터 18시까지 운영하므로, 9개의 예약가능 시간을 true로 구성함
    )

class ReservationMenu{
    val unionReservation : MutableMap<MeetingRoom, Reservation> = mutableMapOf()
    init{
        MeetingRoom.values().forEach{ it ->
            unionReservation[it] = Reservation(date=LocalDate.now(), phoneNumber = "")//예약 초기화
        }
    }
    fun makeReservation(office:MeetingRoom, date:LocalDate, startTime:Int, endTime: Int,
                        phoneNumber: String):Boolean {
        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(7.toLong()))) {
            println("예약 불가.") // 오늘 이전과 오늘부터 7일 이후에 예약하려 한 경우
            return false
        }
        if (startTime !in (9..17) || endTime !in (9..17) || startTime >= endTime) {
            println("잘못된 예약 시간을 입력함.")
            return false
        }
        for (t in (startTime - 9) until (endTime - 9)) {
            unionReservation[office]!!.availableTimes[t]=false
        }
        println("예약이 완료되었습니다.")
        return true

    }

}