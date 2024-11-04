package com.sesac.officeroom.presentation.common

import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*

/**
 * Console input
 *
 * Singleton
 */
object Input {
    private val scanner = Scanner(System.`in`)
    fun isString():String{
        return try {
            scanner.nextLine()
        } catch (e: Exception) {
            println(Strings.ERROR_MESSAGE)
            ""
        }
    }

    fun isInt(): Int{
        return try {
            scanner.nextLine().toInt()
        } catch (e:NumberFormatException) {
            -1
        } catch (e:Exception) {
            -1
        }
    }








}