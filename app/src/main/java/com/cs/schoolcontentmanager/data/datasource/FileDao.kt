package com.cs.schoolcontentmanager.data.datasource

import androidx.room.*
import com.cs.schoolcontentmanager.domain.model.File
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(file: File)

    @Query("SELECT * FROM file ORDER BY id DESC")
    fun getAllFiles(): Flow<List<File>>

    @Query("SELECT * FROM file LIMIT :size")
    fun getAllFiles(size: Int): Flow<List<File>>

    @Query("SELECT * FROM file WHERE name LIKE :name")
    fun getFilesByName(name: String): Flow<List<File>>

    @Delete
    suspend fun delete(file: File)
}