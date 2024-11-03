package com.sesac.officeroom.oldproject.presentation.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object View {

    /**
     * pretty print console
     *
     * @param message
     */
    fun prettyPrintConsole(message: String) {
        val width = 80 // 너비 설정

        val lines = message.lines()

        // 상단 테두리 출력
        print("┌")
        for (i in 0 until width) {
            print("─")
        }
        println("┐")

        // 각 줄을 가운데 정렬하여 출력
        for (line in lines) {
            if (line.length > width) {
                // 줄의 길이가 설정한 너비보다 길면 그대로 출력 (혹은 필요에 따라 자를 수 있음)
                println(line.substring(0, width))
            } else {
                // 줄의 길이가 너비보다 작으면 가운데 정렬
                val paddingSize = (width - line.length) / 2 // 각 줄의 앞뒤로 들어갈 공백 계산
                var paddedLine = " ".repeat(paddingSize.coerceAtLeast(0)) + line // 앞쪽 공백 추가
                paddedLine += " ".repeat(width - paddedLine.length) // 뒤쪽 공백 추가

                // 가운데 정렬된 줄을 출력
                print(String.format("%-${width}s%n", paddedLine))
            }
        }

        // 하단 테두리 출력
        print("└")
        for (i in 0 until width) {
            print("─")
        }
        println("┘")
    }

    /**
     * showDates()함수: 현재 날짜를 포함하여 7일을 보여주는 함수
     */
    fun showDates(){
        val today = LocalDate.now() //today 는 24-10-30 형식
        for (i in 0 until 7){ //i는 0부터 6까지
            val date = today.plusDays(i.toLong()) //오늘을 포함해야 하므로 i가 0부터 시작해야 함
            val dayKor = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN) //요일을 가져와서 '수요일' 처럼 만듦
            println("${i+1}. ${date.format(DateTimeFormatter.ofPattern("MM/dd"))} $dayKor") //1. 10/30 수요일 형식으로 출력
        }

    }
}