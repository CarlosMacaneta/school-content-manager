package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow

class GetLocalFiles(
    private val repository: FileRepository
) {
    operator fun invoke(): Flow<List<File>> {
        return repository.getAllFiles()
    }
}