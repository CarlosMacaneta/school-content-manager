package com.cs.schoolcontentmanager.data.datasource

import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class FileDataSource
@Inject constructor(
    storageRef: StorageReference,
    dbRef: DatabaseReference
){
    fun uploadFile() {

    }
}