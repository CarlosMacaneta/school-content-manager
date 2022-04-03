package com.cs.schoolcontentmanager.ui.home.upload

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cs.schoolcontentmanager.databinding.FragmentUploadFileBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadFilesFragment : Fragment() {

    private lateinit var binding: FragmentUploadFileBinding

    private lateinit var storageRef: StorageReference
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUploadFileBinding.inflate(layoutInflater)

        val launcher: ActivityResultLauncher<String> = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            if (it != null) {
                uploadFile(it)
            }
        }
        binding.btnUpload.setOnClickListener {
            launcher.launch("application/*")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageRef = FirebaseStorage.getInstance().reference
        dbRef = FirebaseDatabase.getInstance().getReference("uploads")

    }


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
}