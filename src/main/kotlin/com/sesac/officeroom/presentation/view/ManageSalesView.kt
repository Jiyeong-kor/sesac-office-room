package com.sesac.officeroom.presentation.view

import com.sesac.officeroom.presentation.common.Input
import com.sesac.officeroom.presentation.common.Strings
import com.sesac.officeroom.presentation.common.View
import com.sesac.officeroom.presentation.viewmodel.ManageSalesViewModel
import com.sesac.officeroom.repository.ManageOfficeRepositoryImpl
import kotlinx.coroutines.runBlocking

/**
 * 메인 > [2]매출 관리
 *
 * desc: 매출 관리 process
 */
//private val viewModel: ManageSalesViewModel)
class ManageSalesView{
    private val viewModel = ManageSalesViewModel(ManageOfficeRepositoryImpl())
    fun main() = runBlocking {
        while (true) {
            View.prettyPrintConsole(Strings.STEP_2_MENU_MESSAGE)
            when (Input.isInt()) {
                1 -> viewModel.displayTotalOfficesSales()
                2 -> viewModel.displaySalesByOffice()
                3 -> viewModel.displaySalesByDate()
                4 -> viewModel.displayTotalUsers()
                0 -> break
                else -> View.prettyPrintConsole(Strings.ERROR_MESSAGE)
            }
        }
    }
}