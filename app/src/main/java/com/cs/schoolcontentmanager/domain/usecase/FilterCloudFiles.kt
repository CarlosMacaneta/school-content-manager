package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class FilterCloudFiles
@Inject constructor(
    private val repository: FileRepository
) {
    operator fun invoke(
        subject: String,
        getFiles: (files: MutableList<File>) -> Unit
    ) {
        repository.getFiles().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<File>()

                snapshot.children.forEach() {
                    it.getValue(File::class.java)?.let { file ->
                        if (file.subject.equals(subject, true)) temp.add(file)
                    }
                }
                getFiles(temp.shuffled().toMutableList())
            }
            override fun onCancelled(error: DatabaseError) {
                throw Exception("Error fetching data from the server, " +
                        "make sure that you are connected to internet...")
            }
        })
    }
}