package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.cs.schoolcontentmanager.utils.Constants.CONTENT
import timber.log.Timber
import java.io.File


object FileSetup {

    val mimeTypes = arrayOf(
        "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
        "application/pdf", "image/png", "image/jpeg"
    )

    fun intentFileChooser(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        return intent
    }

    @SuppressLint("Recycle", "Range")
    fun fileName(uri: Uri, context: Context): String? {
        var res: String? = null
        uri.scheme?.let {
            if (uri.scheme.equals(CONTENT)) {
                val cursor = context.contentResolver.query(uri, null,
                    null, null, null)

                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        res = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } catch (exc: Exception) {
                    Timber.e(exc.localizedMessage)
                } finally {
                    cursor?.close()
                }

                if (res == null) {
                    val cutt = uri.path?.lastIndexOf("/")

                    if (cutt != -1) {
                        res = cutt?.plus(1)?.let { num -> uri.path?.substring(num) }
                    }
                }
            }
        }

        return res
    }

    fun fileExtension(fileName: String): String = fileName.split(".").last()

    fun extension(context: Context, uri: Uri): String? {
        val cR: ContentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    fun fileSize(context: Context, uri: Uri): String? {
        var fileSize: String? = null
        val cursor: Cursor? = context.contentResolver
            .query(uri, null, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {

                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
                if (!it.isNull(sizeIndex)) {
                    fileSize = it.getString(sizeIndex)
                }
            }
        }
        return fileSize
    }

    @Suppress("DEPRECATION")
    fun getOutputDirectory(context: Context, folderName: String): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, folderName).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }
}