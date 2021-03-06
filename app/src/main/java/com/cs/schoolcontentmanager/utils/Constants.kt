package com.cs.schoolcontentmanager.utils

import com.cs.schoolcontentmanager.BuildConfig

object Constants {

    const val FILE_PROVIDER_AUTHORITY: String = BuildConfig.APPLICATION_ID + ".provider"
    const val BS_OPTIONS = "BS_OPTIONS"
    const val BS_FILTER = "filter"

    const val FOLDER = "scm_scan"
    const val FOLDER_FILES = "scm_files"
    const val FILE_NAME_KEY = "scm_scan_"
    const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    const val PHOTO_EXTENSION = ".jpg"
    const val RATIO_4_3_VALUE = 4.0 / 3.0
    const val RATIO_16_9_VALUE = 16.0 / 9.0
    const val RATION_16_VALUE = 16F
    const val RATION_9_VALUE = 9F

    const val UPLOADS = "uploads"

    const val CONTENT = "content"
    const val FILE_NAME = "fileName"
    const val FILE_URI = "fileUri"
    const val FILE_SIZE = "fileSize"
    const val FILE_STAMPS = "fileStamps"
    

    const val PROGRESS_MAX = 100
    const val PROGRESS_CURRENT = 0

    const val CHANNEL_ID = "upDown"
    const val CHANNEL_NAME = "uploadOrDownload"
    const val NOTIFICATION_ID = 1

    const val COURSES = "courses"

    const val LIST_VIEW = "list_view"
    const val GRID_VIEW = "grid_view"

    const val DOWNLOADED_FILES_VIEW = "downloaded_view"
    const val CLOUD_FILES_VIEW = "cloud_view"

    //file extension
    const val PDF = ".pdf"
    const val PNG = ".png"
    const val JPEG = ".jpeg"
    const val JPG = ".jpg"
    const val DOC = ".doc"
    const val DOCX = ".docx"
    const val XLS = ".xls"
    const val XLSX = ".xlsx"
    const val PPT = ".ppt"
    const val PPTX = ".pptx"

    //
    const val WORD = "https://play.google.com/store/apps/details?id=com.microsoft.office.word&hl=en&gl=US"
    const val EXCEL = "https://play.google.com/store/apps/details?id=com.microsoft.office.excel&hl=en&gl=US"
    const val POWER_POINT = "https://play.google.com/store/apps/details?id=com.microsoft.office.powerpoint&hl=en&gl=US"
}