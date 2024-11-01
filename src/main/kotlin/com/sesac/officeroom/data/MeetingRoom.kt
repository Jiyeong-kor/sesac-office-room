package com.sesac.officeroom.data

/**
 * 회의실 정보를 담고있는 enum class
 */
enum class MeetingRoom ( // data class ->  txt 파일 읽어들이기
    val minCapacity: Int, //최소 인원
    val maxCapacity: Int, //최대 수용 가능 인원
    val baseCostPerHour: Int, //시간당 기본요금
    val hasWindow: Boolean, //창문 유무
    val hasPhotoBooth: Boolean //촬영부스 유무
){
    JJANG_GOOD_ROOM(2,4,5000,true,true),
    //짱좋은 회의실: 최소인원 2명, 최대인원 4명, 기본요금 5000원, 창문있음, 포토부스 있음
    VERY_GOOD_ROOM(3,6,3000,true,false),
    //좋은 회의실: 최소인원 3명, 최대인원 6명, 기본요금 3000원, 창문있음, 포토부스 없음
    LESS_GOOD_ROOM(4,12,2000,false,false)
    //덜좋은 회의실: 최소인원 4명, 최대인원 12명, 기본요금 2000원, 창문이랑 포토부스 없음
}