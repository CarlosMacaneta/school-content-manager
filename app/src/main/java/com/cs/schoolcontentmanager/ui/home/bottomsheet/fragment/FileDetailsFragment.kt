package com.cs.schoolcontentmanager.ui.home.bottomsheet.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.cs.schoolcontentmanager.databinding.DialogFileBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.FileSetup
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME
import com.cs.schoolcontentmanager.utils.Constants.UPLOADS
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FileDetailsFragment: DialogFragment() {

    private lateinit var binding: DialogFileBinding

    @Inject lateinit var storageRef: StorageReference
    @Inject lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btClose.setOnClickListener { dismiss() }

        arguments?.getString(FILE_NAME)?.let {
            binding.tfFileName.suffixText = ".${FileSetup.fileExtension(it)}"
            binding.tfFileName.editText?.setText(it.split('.')[0])
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun uploadFile(data: Uri) {
        Snackbar.make(binding.root, data.toString(), Snackbar.LENGTH_LONG).show()

        val ref = storageRef.child("${UPLOADS}/${System.currentTimeMillis()}")
        ref.putFile(data).addOnSuccessListener {

            val uri = it.storage.downloadUrl

            while (!uri.isComplete);
            val file = File(
                binding.tfFileName.editText?.text.toString(),
                binding.tfDescription.editText?.text.toString(),
                uri.result.toString()
            )

            dbRef.push().key?.let { key -> dbRef.child(key).setValue(file) }

            Snackbar.make(binding.root, "File uploaded successful", Snackbar.LENGTH_LONG).show()
            //close progress
        }
    }
}