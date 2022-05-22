package com.cs.schoolcontentmanager.domain.usecase

import android.net.Uri
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class UploadFile
@Inject constructor(
    private val repository: FileRepository
){
    operator fun invoke(
        uri: Uri,
        onSuccessListener: (uri: Task<Uri>) -> Unit,
        onProgressListener: (progress: Double) -> Unit
    ) {
        repository.uploadFile(uri).addOnSuccessListener {
            onSuccessListener(it.storage.downloadUrl)
        }.addOnProgressListener {
            onProgressListener((100.0 * it.bytesTransferred) / it.totalByteCount)
        }
    }
}