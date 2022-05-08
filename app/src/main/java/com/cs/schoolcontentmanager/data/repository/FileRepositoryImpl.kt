package com.cs.schoolcontentmanager.data.repository

import android.net.Uri
import com.cs.schoolcontentmanager.data.datasource.FileDataSource
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl
 @Inject constructor(
     private val dsFile: FileDataSource
 ) : FileRepository {
    override suspend fun uploadFile(uri: Uri) = dsFile.uploadFile(uri)

    override fun getFiles() = dsFile.getFiles()

    override suspend fun getCourses() = dsFile.getCourses()

    override suspend fun downloadFile(url: String) = dsFile.downloadFile(url)
}