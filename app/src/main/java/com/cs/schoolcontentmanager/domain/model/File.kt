package com.cs.schoolcontentmanager.domain.model

data class File(
    val name: String,
    val description: String? = null,
    val uri: String
)
