package com.sesac.officeroom.newproject.presentation.common

import java.util.*

/**
 * Console input
 *
 * Singleton
 */
object Input {
    private val scanner = Scanner(System.`in`)
    fun isString() = scanner.nextLine()
    fun isInt() = scanner.nextLine().toInt()
}