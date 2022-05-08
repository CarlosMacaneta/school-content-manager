package com.cs.schoolcontentmanager.domain.usecase

import com.cs.schoolcontentmanager.domain.repository.FileRepository

class GetCourses(
    private val repository: FileRepository
) {
    suspend operator fun invoke() = repository.getCourses()
}