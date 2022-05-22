package com.cs.schoolcontentmanager.domain.repository

import android.net.Uri
import com.cs.schoolcontentmanager.domain.model.Course
import com.cs.schoolcontentmanager.domain.model.File
import com.google.firebase.database.Query
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.flow.Flow

interface FileRepository {

    suspend fun create(file: File)

    fun uploadFile(uri: Uri): UploadTask

    fun getFiles(): Query

    fun getFilesByName(value: String): Flow<List<File>>

    fun getAllFiles(): Flow<List<File>>

    fun getAllFiles(size: Int): Flow<List<File>>

    suspend fun getCourses(): MutableList<Course>

    suspend fun downloadFile(url: String): Uri

    suspend fun destroy(file: File)
}