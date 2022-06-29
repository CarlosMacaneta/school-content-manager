package com.cs.schoolcontentmanager.data.repository

import androidx.datastore.core.DataStore
import com.cs.schoolcontentmanager.Settings
import com.cs.schoolcontentmanager.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException

class SettingsRepositoryImpl(
    private val settings: DataStore<Settings>
): SettingsRepository {

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        settings.updateData {
            it.toBuilder().setDarkMode(isDarkMode).build()
        }
    }

    override suspend fun setLanguage(language: String) {
        settings.updateData {
            it.toBuilder().setLang(language).build()
        }
    }

    override suspend fun readConfig() =
        settings.data.catch {
            if (it is IOException) {
                Timber.tag(TAG).e("Error reading settings.")
                emit(Settings.getDefaultInstance())
            } else {
                throw it
            }
        }

    companion object {
        private const val TAG: String = "SettingsRepo"
    }
}