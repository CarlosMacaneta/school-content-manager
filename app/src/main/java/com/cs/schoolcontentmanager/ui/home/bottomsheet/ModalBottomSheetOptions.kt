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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.cs.schoolcontentmanager.databinding.BottomSheetOptionsDialogBinding
import com.cs.schoolcontentmanager.utils.Constants.mimeTypes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@ActivityScoped
class ModalBottomSheetOptions @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetOptionsDialogBinding

    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomSheetOptionsDialogBinding.inflate(layoutInflater)

        cameraPermission()

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
            launchCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
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

    private fun cameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                listOf(Manifest.permission.CAMERA).toTypedArray(),
                100
            )
        }
    }
}