package com.cs.schoolcontentmanager.ui.home.bottomsheet.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import com.cs.schoolcontentmanager.utils.Constants.FILENAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME_KEY
import com.cs.schoolcontentmanager.utils.Constants.PHOTO_EXTENSION
import java.io.File
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
        executorService: Executor,
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
        context: Context,
        setOnSuccessListener: () -> Unit
    ) {
        val photoFile = createFile(outputDirectory)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOptions,
            executorService,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        context.sendBroadcast(
                            Intent(Camera.ACTION_NEW_PICTURE, savedUri)
                        )
                    }

                    val mimeType = MimeTypeMap.getSingleton()
                        .getMimeTypeFromExtension(savedUri.toFile().extension)
                    MediaScannerConnection.scanFile(
                        context,
                        arrayOf(savedUri.toFile().absolutePath),
                        arrayOf(mimeType)
                    ) { _, uri ->
                        Log.d("TAG", "Image capture scanned into media store: $uri")
                    }
                    setOnSuccessListener()
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e("TAG", "Photo capture failed: ${exc.message}", exc)
                }
            })
    }

    private fun createFile(baseFolder: File) =
        File(baseFolder, FILE_NAME_KEY + SimpleDateFormat(FILENAME, Locale.US)
            .format(System.currentTimeMillis()) + PHOTO_EXTENSION)
}