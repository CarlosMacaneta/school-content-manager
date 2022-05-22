package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.utils.Constants.FILENAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME_KEY
import com.cs.schoolcontentmanager.utils.Constants.PHOTO_EXTENSION
import com.yalantis.ucrop.UCrop
import timber.log.Timber
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor


object CameraSetup {

    fun cameraPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(context as Activity,
                listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
                201
            )
        }
    }

    fun bindPreview(
        provider: ProcessCameraProvider,
        imageCapture: ImageCapture,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        provider.unbindAll()

        val preview: Preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        provider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
    }

    @Suppress("DEPRECATION")
    fun imgCapture(
        outputDirectory: File,
        imageCapture: ImageCapture,
        executorService: Executor,
        setOnSuccessListener: (bitmap: Bitmap) -> Unit
    ) {
        val photoFile = createFile(outputDirectory)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(
            executorService,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    setOnSuccessListener(imageProxyToBitmap(image))
                }

                override fun onError(exc: ImageCaptureException) {
                    Timber.e(exc, "Photo capture failed: " + exc.message)
                }
            })
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun createFile(baseFolder: File) =
        File(baseFolder, FILE_NAME_KEY + SimpleDateFormat(FILENAME, Locale.US)
            .format(System.currentTimeMillis()) + PHOTO_EXTENSION)

    fun crop(
        context: Context,
        source: Uri,
        fragment: Fragment
    ) {
        val options = UCrop.Options()
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);

        options.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setToolbarWidgetColor(ContextCompat.getColor(context, R.color.white))
        options.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(context, R.color.colorPrimary))
        options.setShowCropFrame(true)
        options.setFreeStyleCropEnabled(true)

        val uCrop = UCrop.of(source, Uri.fromFile(File(context.cacheDir, "${UUID.randomUUID()}${PHOTO_EXTENSION}")))
            .withOptions(options)

        uCrop.start(context, fragment)
    }
}