package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.getOutputDirectory
import com.cs.schoolcontentmanager.utils.Constants
import com.cs.schoolcontentmanager.utils.Constants.FILE_PROVIDER_AUTHORITY
import com.cs.schoolcontentmanager.utils.Constants.FOLDER
import com.cs.schoolcontentmanager.utils.Constants.PDF
import com.pspdfkit.document.processor.*
import com.pspdfkit.utils.Size
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object GeneratePDF {

    fun convertImgToPdf(context: Context, image: Bitmap): Uri? {
        val pdf = createFile(getOutputDirectory(context, FOLDER))
        val imageSize = Size(image.width.toFloat(), image.height.toFloat())
        val pageImage = PageImage(image, PagePosition.CENTER)
        pageImage.rotation = -90
        pageImage.setJpegQuality(70)
        val newPage = NewPage
            .emptyPage(imageSize)
            .withPageItem(pageImage)
            .build()

        val task = PdfProcessorTask.newPage(newPage)

        val disposable = PdfProcessor.processDocumentAsync(task, pdf)
            .subscribe { progress ->
                Timber.e("$progress")
            }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                FILE_PROVIDER_AUTHORITY,
                pdf
            )
        } else {
            Uri.fromFile(pdf)
        }
    }

    private fun createFile(baseFolder: File) =
        File(baseFolder, Constants.FILE_NAME_KEY + SimpleDateFormat(Constants.FILENAME, Locale.US)
            .format(System.currentTimeMillis()) + PDF
        )
}