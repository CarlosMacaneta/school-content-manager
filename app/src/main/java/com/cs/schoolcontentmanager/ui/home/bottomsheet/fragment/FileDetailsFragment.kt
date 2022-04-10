package com.cs.schoolcontentmanager.ui.home.bottomsheet.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationCompat.PRIORITY_LOW
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import com.cs.schoolcontentmanager.data.DbUtil.setCustomMetadata
import com.cs.schoolcontentmanager.databinding.DialogFileBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.ui.home.bottomsheet.util.FileSetup.fileExtension
import com.cs.schoolcontentmanager.utils.Constants
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_URI
import com.cs.schoolcontentmanager.utils.Constants.UPLOADS
import com.cs.schoolcontentmanager.utils.Util.notificationBuilder
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
            binding.tfFileName.suffixText = ".${fileExtension(it)}"
            binding.tfFileName.editText?.setText(it.split('.')[0])
        }

        val uri = arguments?.getString(FILE_URI)

        binding.btUpload.setOnClickListener {
            uri?.let { uploadFile(Uri.parse(it)) }
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun uploadFile(data: Uri) {
        val builder = notificationBuilder(requireContext(), true)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        builder.setProgress(Constants.PROGRESS_MAX, Constants.PROGRESS_CURRENT, false)
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())

        val metadata = setCustomMetadata()

        val ref = storageRef.child("${UPLOADS}/${System.currentTimeMillis()}")
        ref.putFile(data, metadata).addOnSuccessListener {

            val uri = it.storage.downloadUrl

            while (!uri.isComplete);
            val file = File(
                binding.tfFileName.editText?.text.toString(),
                binding.tfDescription.editText?.text.toString(),
                uri.result.toString()
            )

            dbRef.push().key?.let { key -> dbRef.child(key).setValue(file) }

            builder.setProgress(0, 0, false)
            builder.setContentText("Upload complete")
            builder.priority = PRIORITY_DEFAULT
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
        }.addOnProgressListener {

            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount

            builder.priority = PRIORITY_LOW
            builder.setProgress(Constants.PROGRESS_MAX, progress.toInt(), false)
            builder.setContentText("${progress.toInt()}%")
            notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
        }
    }
}