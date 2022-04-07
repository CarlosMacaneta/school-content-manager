package com.cs.schoolcontentmanager.ui.home.bottomsheet

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.cs.schoolcontentmanager.databinding.BottomSheetOptionsDialogBinding
import com.cs.schoolcontentmanager.ui.home.bottomsheet.fragment.ScanFragment
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.CameraSetup.cameraPermission
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.ImageAnalyzer
import com.cs.schoolcontentmanager.utils.Constants.mimeTypes
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.ExecutorService
import javax.inject.Inject


@ActivityScoped
class ModalBottomSheetOptions @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetOptionsDialogBinding

    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var executorService: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var imageAnalyser: ImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomSheetOptionsDialogBinding.inflate(layoutInflater)

        val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data.let {
                    Toast.makeText(requireContext(), it?.data.toString(), Toast.LENGTH_LONG).show()
                }
                //uploadFile(it)
            }
        }

        val launchCamera: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result.data.let {
                    val img: Bitmap = it?.extras?.get("data") as Bitmap


                    Toast.makeText(requireContext(), img.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnUpload.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.putExtra(EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(CATEGORY_OPENABLE)
            intent.putExtra(EXTRA_LOCAL_ONLY, true)
            launcher.launch(intent)
        }

        binding.btnScan.setOnClickListener {
            //launchCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))\
            openCamera()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    private fun uploadFile(data: Uri) {
        Snackbar.make(binding.root, data.toString(), Snackbar.LENGTH_LONG).show()

        /*val ref = storageRef.child("upload/${System.currentTimeMillis()}")
        ref.putFile(data).addOnSuccessListener {

            val uri = it.storage.downloadUrl

            while (!uri.isComplete);
            val file = File(binding.fileName.text.toString(), uri.result.toString())

            dbRef.push().key?.let { key -> dbRef.child(key).setValue(file) }

            Snackbar.make(binding.root, "File uploaded successful", Snackbar.LENGTH_LONG).show()
            //close progress
        }.addOnProgressListener {
            //start progress
        }*/
    }

    private fun openCamera() {

        val fragmentManager = requireActivity().supportFragmentManager

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, ScanFragment()).addToBackStack(null).commit()

        dismiss()
    }

    private fun getText(bitmap: Bitmap) {
        val recognizer = TextRecognizer.Builder(requireContext()).build()

        if (!recognizer.isOperational) {
            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
        } else {
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlocks: SparseArray<TextBlock> = recognizer.detect(frame)

            val text = StringBuilder()

            textBlocks.forEach { _, value -> text.append(value).append("\n") }

            Toast.makeText(requireContext(), text.toString(), Toast.LENGTH_LONG).show()
        }
    }
}