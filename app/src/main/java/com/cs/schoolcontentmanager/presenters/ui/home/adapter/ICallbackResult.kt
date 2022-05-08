package com.cs.schoolcontentmanager.presenters.ui.home.adapter

import android.view.View
import com.cs.schoolcontentmanager.domain.model.File

interface ICallbackResult {
    fun getResultCallback(itemView: View, position: Int, file: File)
}