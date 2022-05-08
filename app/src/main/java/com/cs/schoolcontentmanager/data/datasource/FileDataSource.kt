package com.cs.schoolcontentmanager.data.datasource

import android.net.Uri
import com.cs.schoolcontentmanager.domain.model.Course
import com.cs.schoolcontentmanager.utils.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FileDataSource
@Inject constructor(
    private val storageRef: StorageReference,
    private val dbRef: DatabaseReference,
    private val coursesDoc: CollectionReference
){
    suspend fun uploadFile(uri: Uri): UploadTask.TaskSnapshot? {
        val ref = storageRef.child("${Constants.UPLOADS}/${System.currentTimeMillis()}")
        return ref.putFile(uri).await()
    }

    fun getFiles(): Query = dbRef.orderByValue()

    suspend fun getCourses(): MutableList<Course>  = coursesDoc.get().await().toObjects(Course::class.java)

    suspend fun downloadFile(url: String): Uri = storageRef.child(url).downloadUrl.await()
}