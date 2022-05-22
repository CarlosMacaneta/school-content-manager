package com.cs.schoolcontentmanager.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class File(
    val name: String = "",
    val type: String = "",
    val subject: String = "",
    val uri: String = "",
    val publishedBy: String = "",
    @PrimaryKey val id: Int? = null
)
