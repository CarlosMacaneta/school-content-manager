package com.cs.schoolcontentmanager.presenters.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.cs.schoolcontentmanager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val lang = findPreference<ListPreference>("lang")
        val theme = findPreference<SwitchPreferenceCompat>("theme")

        lang?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), lang.value, Toast.LENGTH_SHORT).show()
            true
        }

        theme?.setOnPreferenceClickListener {
            if (theme.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            true
        }
    }
}