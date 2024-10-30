package com.sesac.officeroom.view

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
                System.out.printf("%-" + width + "s%n", paddedLine)
            }
        }

        // 하단 테두리 출력
        print("└")
        for (i in 0 until width) {
            print("─")
        }
        println("┘")
    }
}