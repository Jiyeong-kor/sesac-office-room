package com.sesac.officeroom.newproject.presentation.view

import com.sesac.officeroom.oldproject.presentation.common.Input
import com.sesac.officeroom.oldproject.presentation.common.Strings
import com.sesac.officeroom.oldproject.presentation.common.View

/**
 * 메인 > [2]매출 관리
 *
 * desc: 매출 관리 process
 */
class ManageSalesView {

    fun main() {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_2_MENU_MESSAGE)

            when (Input.isInt()) {
                1 -> {}
                2 -> {}
                3 -> {}
                4 -> {}
                0 -> break
            }
        }
    }
}