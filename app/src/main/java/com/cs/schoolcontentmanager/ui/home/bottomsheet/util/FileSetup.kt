package com.cs.schoolcontentmanager.ui.home.bottomsheet.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import com.cs.schoolcontentmanager.utils.Constants.CONTENT


object FileSetup {

    private val mimeTypes = arrayOf(
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
                    Log.e("fileName", exc.localizedMessage as String)
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
}