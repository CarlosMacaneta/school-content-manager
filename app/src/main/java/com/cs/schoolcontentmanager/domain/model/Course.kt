package com.cs.schoolcontentmanager.domain.model

data class Course(
    val dept: String = "",
    val name: String = "",
    val subjects: List<Subject> = mutableListOf()
) {
    override fun toString() = name
}