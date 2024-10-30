package com.sesac.officeroom.view

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