package com.cs.schoolcontentmanager.utils

object Constants {

    const val BS_OPTIONS = "BS_OPTIONS"

    val mimeTypes = arrayOf(
        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
        "application/pdf", "image/png", "image/jpeg"
    )
}