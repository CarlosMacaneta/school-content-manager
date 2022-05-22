package com.cs.schoolcontentmanager.data.repository

import android.net.Uri
import com.cs.schoolcontentmanager.data.datasource.FileDao
import com.cs.schoolcontentmanager.data.datasource.FileDataSource
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import javax.inject.Inject

class FileRepositoryImpl
 @Inject constructor(
     private val dsFile: FileDataSource,
     private val fileDao: FileDao
 ) : FileRepository {
    override suspend fun create(file: File) {
        fileDao.create(file)
    }

    override fun uploadFile(uri: Uri) = dsFile.uploadFile(uri)

    override fun getFiles() = dsFile.getFiles()

    override fun getFilesByName(value: String) = fileDao.getFilesByName(value)

    override fun getAllFiles() = fileDao.getAllFiles()

    override fun getAllFiles(size: Int) = fileDao.getAllFiles(size)

    override suspend fun getCourses() = dsFile.getCourses()

    override suspend fun downloadFile(url: String) = dsFile.downloadFile(url)

    override suspend fun destroy(file: File) {
        fileDao.delete(file)
    }
}