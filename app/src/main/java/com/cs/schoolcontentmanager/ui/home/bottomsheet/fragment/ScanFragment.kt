package com.cs.schoolcontentmanager.ui.home.bottomsheet.fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.cs.schoolcontentmanager.databinding.PreviewScanBinding
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup.cameraPermission
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup.imgCapture
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.ImageAnalyzer
import com.cs.schoolcontentmanager.utils.Constants.FOLDER
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor

class ScanFragment: DialogFragment() {

    private lateinit var binding: PreviewScanBinding

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageAnalyser: ImageAnalyzer
    private lateinit var outputDirectory: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PreviewScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = binding.cameraSan
        imageAnalyser = ImageAnalyzer()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            .setTargetRotation(view.display.rotation)
            .build()

        setupCamera()

        outputDirectory = getOutputDirectory(requireContext())

        binding.btCapture.setOnClickListener {
            imgCapture(
                outputDirectory,
                imageCapture,
                executor(),
                requireContext()) { dismiss() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    private fun executor(): Executor = ContextCompat.getMainExecutor(requireContext())

    private fun setupCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            cameraProviderFuture.addListener({
                try {
                    val provider: ProcessCameraProvider = cameraProviderFuture.get()
                    CameraSetup.bindPreview(
                        provider,
                        imageCapture,
                        previewView,
                        this
                    )
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }, executor())
        } else cameraPermission(requireContext())
    }

    @Suppress("DEPRECATION")
    private fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, FOLDER).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }
}