package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.repository.FileRepository

class SearchLocalFileByName(
    private val repository: FileRepository
) {
    operator fun invoke(name: String) = repository.getFilesByName(name)
}