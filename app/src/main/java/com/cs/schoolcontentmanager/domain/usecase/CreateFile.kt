package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository

class CreateFile(
    private val repository: FileRepository
) {
    suspend operator fun invoke(file: File) {
        repository.create(file)
    }
}