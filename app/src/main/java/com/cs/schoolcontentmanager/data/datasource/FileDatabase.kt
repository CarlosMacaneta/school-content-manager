package com.cs.schoolcontentmanager.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cs.schoolcontentmanager.domain.model.File

@Database(
    entities = [File::class],
    version  = 1
)
abstract class FileDatabase: RoomDatabase() {
    abstract val fileDao: FileDao

    companion object {
        const val DB_NAME = "files_db"
    }
}