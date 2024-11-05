package com.sesac.officeroom.data.source

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File


class OfficeDataSourceImpl() : OfficeDataSource {

    private val ioDispatcher = CoroutineScope(Dispatchers.IO)

    /**
     * Read office.txt
     *
     * @return String
     */
    override suspend fun readOfficeTxt(): File {
         return runBlocking {
             var result: File? = null
             ioDispatcher.async {
                runCatching {
                    result = File("C:\\Users\\cjh19\\Documents\\GitHub\\sesac-office-room\\src\\main\\kotlin\\com\\sesac\\officeroom\\data\\Office.txt")
                }.onFailure {
                    println("readOfficeTxt() : IOException ")
                }.onSuccess {
                    println("readOfficeTxt() : Success ")
                }
             }.await()
             result!!
        }
    }
}