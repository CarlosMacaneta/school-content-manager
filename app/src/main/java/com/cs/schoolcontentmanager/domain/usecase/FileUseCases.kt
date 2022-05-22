package com.cs.schoolcontentmanager.domain.usecase

data class FileUseCases(
    val createFile: CreateFile,
    val uploadFile: UploadFile,
    val getFiles: GetFiles,
    val searchFiles: SearchCloudFiles,
    val searchLocalFileByName: SearchLocalFileByName,
    val downloadFile: DownloadFile,
    val getCourses: GetCourses,
    val getLocalFiles: GetLocalFiles,
    val deleteFile: DeleteFile
)
