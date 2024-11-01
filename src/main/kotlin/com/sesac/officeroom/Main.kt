package com.sesac.officeroom

import com.sesac.officeroom.view.OfficeManagerView
import com.sesac.officeroom.viewmodel.OfficeManager
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream


/**
 * Main
 */
fun main() {
    //val manager = OfficeManager()
    //manager.mainProcess()

    val manager = OfficeManagerView()
    manager.mainProcess()


    //val file = File("src/main/kotlin/com/sesac/officeroom/files/OfficeManager.txt")//.bufferedWriter(Charsets.UTF_8)
    //file.readLines().forEach { println(it) }

    /*if (!file.exists()) {

    }
    val dis = DataInputStream(FileInputStream(fileOut))*/

    /*println("콤마를 기준으로 이름, 국어, 영어, 수학 점수를 입력하시고 엔터를 치세요(ex::표인수,45,67,48), 끝내려면 exit 를 입력!")
    var line: String?
    line = Input.isString()
    while (!line.isNullOrEmpty() && !line.equals("exit", true)) {
        val gradeList = line.split(",")
        val gradeAverage = gradeList.filterIndexed { index, _ ->
            index != 0
        }.map {
            var grade = -1
            try {
                grade = it.trim().toInt()
            } catch (_: NumberFormatException) { }
            grade
        }.average().roundToInt()
        val resultLine = """$line : $gradeAverage"""
        with(fileOut) {
            write(resultLine)
            newLine()
            flush()
            println(resultLine)
        }
        line = Input.isString()
    }
    println("성적 계산 종료!")
    fileOut.flush()
    fileOut.close()*/

}
