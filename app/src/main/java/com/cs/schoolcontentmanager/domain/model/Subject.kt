package com.cs.schoolcontentmanager.domain.model

data class Subject(
    val semester: Int = 0,
    val level: Int = 0,
    val name: String = "",
    val shortName: String = ""
) {
    override fun toString() = name
}
