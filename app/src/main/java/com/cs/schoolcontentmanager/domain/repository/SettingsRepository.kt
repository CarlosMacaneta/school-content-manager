package com.cs.schoolcontentmanager.domain.repository

import com.cs.schoolcontentmanager.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun setDarkMode(isDarkMode: Boolean)

    suspend fun setLanguage(language: String)

    suspend fun readConfig(): Flow<Settings>
}