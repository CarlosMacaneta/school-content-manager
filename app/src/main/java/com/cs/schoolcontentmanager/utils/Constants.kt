package com.cs.schoolcontentmanager.utils

object Constants {

    const val BS_OPTIONS = "BS_OPTIONS"

    val mimeTypes = arrayOf(
        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
        "application/pdf", "image/png", "image/jpeg"
    )

    const val FOLDER = "SCMScan"
    const val FILE_NAME_KEY = "scm_scan_"
    const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    const val PHOTO_EXTENSION = ".jpg"
    const val RATIO_4_3_VALUE = 4.0 / 3.0
    const val RATIO_16_9_VALUE = 16.0 / 9.0
    const val RATION_16_VALUE = 16F
    const val RATION_9_VALUE = 9F
}