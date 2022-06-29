package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class GetLocalFiles(
    private val repository: FileRepository
) {
    operator fun invoke(): Flow<List<File>> {
        return repository.getAllFiles()
    }
}