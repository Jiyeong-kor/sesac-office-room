package com.sesac.officeroom.data.source

import java.io.File

interface OfficeDataSource {
    suspend fun readOfficeTxt(): File
}