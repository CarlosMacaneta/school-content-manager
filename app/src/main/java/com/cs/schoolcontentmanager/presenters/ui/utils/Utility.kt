package com.cs.schoolcontentmanager.presenters.ui.utils

import android.content.Intent
import android.net.Uri

object Utility {
    fun intentDownload(name: String) =
        Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(name)
    }
}