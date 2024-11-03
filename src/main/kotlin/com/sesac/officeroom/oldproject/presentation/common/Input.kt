package com.sesac.officeroom.oldproject.presentation.common

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