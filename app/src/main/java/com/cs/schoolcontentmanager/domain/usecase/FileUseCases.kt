package com.cs.schoolcontentmanager.domain.usecase

data class FileUseCases(
    val uploadFile: UploadFile,
    val getFiles: GetFiles,
    val downloadFile: DownloadFile,
    val getCourses: GetCourses
)
