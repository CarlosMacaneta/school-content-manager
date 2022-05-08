package com.cs.schoolcontentmanager.domain.usecase

import android.net.Uri
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class UploadFile
@Inject constructor(
    private val repository: FileRepository
){
    suspend operator fun invoke(uri: Uri): UploadTask.TaskSnapshot? {
        return repository.uploadFile(uri)
    }
}