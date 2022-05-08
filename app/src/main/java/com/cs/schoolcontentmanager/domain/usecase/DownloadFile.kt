package com.cs.schoolcontentmanager.domain.usecase

import android.app.DownloadManager
import android.net.Uri
import android.webkit.CookieManager
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import javax.inject.Inject

class DownloadFile
@Inject constructor(
    private val repository: FileRepository
){
     operator fun invoke(file: File) =
        DownloadManager.Request(Uri.parse(file.uri)).apply {
            setTitle(file.name)
            addRequestHeader(
                "cookie",
                CookieManager.getInstance().getCookie(file.uri)
            )
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
}