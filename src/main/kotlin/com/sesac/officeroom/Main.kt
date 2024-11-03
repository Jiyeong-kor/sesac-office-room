package com.sesac.officeroom

import com.sesac.officeroom.presentation.view.OfficeManagerView

/**
 * Main
 */
suspend fun main() {
    val manager = OfficeManagerView()
    manager.mainProcess()
}
