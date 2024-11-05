package com.sesac.officeroom.data.dto

/**
 * OfficeDTO
 *
 * @property id 회의실 번호
 * @property name 회의실명
 * @property baseCapacity 기준 인원
 * @property maxCapacity 최대 수용 가능 인원
 * @property baseCostPerHour 시간당 기본요금
 * @property additionalCostPerPerson 인당 추가요금
 * @property hasWindow 창문 유무
 * @property hasFilmBooth 촬영부스 유무
 */
data class OfficeDTO(
    val id: Int,
    val name: String,
    val baseCapacity: Int,
    val maxCapacity: Int,
    val baseCostPerHour: Int,
    val additionalCostPerPerson: Int,
    val hasWindow: Boolean,
    val hasFilmBooth: Boolean
)
