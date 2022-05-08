package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class GetFiles (
    private val repository: FileRepository
) {
    private val files = mutableListOf<File>()

    operator fun invoke(): MutableList<File> {
        repository.getFiles().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<File>()
                snapshot.children.forEach {
                    it.getValue(File::class.java)?.let { file -> temp.add(file) }
                }
                getFiles(temp)
            }
            override fun onCancelled(error: DatabaseError) {
                throw Exception("Error fetching data from the server, " +
                        "make sure that you are connected to internet...")
            }
        })

        return files
    }

    private fun getFiles(files: MutableList<File>) {
        this.files.run {
            clear()
            addAll(files)
        }
    }
}