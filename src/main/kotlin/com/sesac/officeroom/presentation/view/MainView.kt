package com.sesac.officeroom.presentation.view

import com.sesac.officeroom.oldproject.presentation.common.Input
import com.sesac.officeroom.oldproject.presentation.common.Strings
import com.sesac.officeroom.oldproject.presentation.common.View

/**
 * 메인 뷰
 */
class MainView {

    fun main() {
        while (true) {
            View.prettyPrintConsole(Strings.MAIN_MESSAGE)

            when(Input.isInt()) {
                1 -> ManageOfficeView().main()
                2 -> ManageSalesView().main()
                0 -> break
                else -> View.prettyPrintConsole(Strings.ERROR_MESSAGE)
            }
        }
    }
}