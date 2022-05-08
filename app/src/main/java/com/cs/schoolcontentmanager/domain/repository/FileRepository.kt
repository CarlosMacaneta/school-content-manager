package com.cs.schoolcontentmanager.domain.repository

import android.net.Uri
import com.cs.schoolcontentmanager.domain.model.Course
import com.google.firebase.database.Query
import com.google.firebase.storage.UploadTask

interface FileRepository {

    suspend fun uploadFile(uri: Uri): UploadTask.TaskSnapshot?

    fun getFiles(): Query

    suspend fun getCourses(): MutableList<Course>

    suspend fun downloadFile(url: String): Uri
}