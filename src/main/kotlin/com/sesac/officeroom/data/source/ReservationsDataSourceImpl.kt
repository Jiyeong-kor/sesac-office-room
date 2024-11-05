package com.sesac.officeroom.data.source

import com.sesac.officeroom.data.ReservationDTO
import com.sesac.officeroom.data.toCSVString
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter

class ReservationsDataSourceImpl(): ReservationsDataSource {

    private val ioDispatcher = CoroutineScope(Dispatchers.IO)

    /**
     * Read Reservations.txt
     *
     * @return
     */
    override suspend fun readReservationsTxt(): File {
        return runBlocking {
            var result: File? = null
            ioDispatcher.async {
                runCatching {
                    val file = File("C:\\Users\\cjh19\\Documents\\GitHub\\sesac-office-room\\src\\main\\kotlin\\com\\sesac\\officeroom\\data\\Reservations.txt")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    result = file
                }.onFailure {
                    //println("readReservationsTxt() : IOException ")
                }.onSuccess {
                    //println("readReservationsTxt() : Success ")
                }
            }.await()
            result!!
        }
    }

    /**
     * save Reservations.txt
     *
     * @param reservationDTO
     * @return Boolean
     */
    override suspend fun saveReservationsTxt(
        reservationDTO: ReservationDTO
    ): Boolean {
        runBlocking {
            var result = false
            ioDispatcher.async {
                runCatching {
                    val file = File("C:\\Users\\cjh19\\Documents\\GitHub\\sesac-office-room\\src\\main\\kotlin\\com\\sesac\\officeroom\\data\\Reservations.txt")
                    if (!file.exists()) {
                        withContext(Dispatchers.IO) {
                            file.createNewFile()
                        }
                    }

                    val fileWriter = FileWriter(file, true)
                    val bufferedWriter = fileWriter.buffered()

                    with(bufferedWriter) {
                        write("${reservationDTO.toCSVString()}\n")
                        flush()
                        close()
                    }
                }.onFailure {
                    result = false
                }.onSuccess {
                    result = true
                }
            }
            return@runBlocking result
        }
        return true
    }
}