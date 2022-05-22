package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationCompat.PRIORITY_LOW
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.DialogFileBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.fileExtension
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.presenters.utils.SpinnerAdapter
import com.cs.schoolcontentmanager.utils.Constants.FILE_NAME
import com.cs.schoolcontentmanager.utils.Constants.FILE_URI
import com.cs.schoolcontentmanager.utils.Constants.NOTIFICATION_ID
import com.cs.schoolcontentmanager.utils.Constants.PROGRESS_CURRENT
import com.cs.schoolcontentmanager.utils.Constants.PROGRESS_MAX
import com.cs.schoolcontentmanager.utils.Util.notificationBuilder
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FileDetailsFragment: DialogFragment() {

    private lateinit var binding: DialogFileBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    @Inject lateinit var dbRef: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        return dialog
    }

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
            binding.tfFileName.editText?.setText(it.split('.').first())
        }

        val uri = arguments?.getString(FILE_URI)

        binding.btUpload.setOnClickListener {
            uri?.let { uploadFile(Uri.parse(it)) }
            dismiss()
        }
        loadData()
    }

    private fun loadData() {
        homeViewModel.courses.observe(viewLifecycleOwner) {
            val adapter = SpinnerAdapter(
                requireContext(),
                R.layout.spinner_item,
                it.toMutableList()
            )
            binding.courseItem.setAdapter(adapter)
            binding.courseItem.setOnItemClickListener { _, _, position, _ ->
                val a = SpinnerAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    it[position].subjects.toMutableList()
                )
                binding.subjectItem.setAdapter(a)
            }
        }
    }

    @Suppress("ControlFlowWithEmptyBody")
    private fun uploadFile(data: Uri) {
        val builder = notificationBuilder(requireContext(), true)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
        notificationManager.notify(NOTIFICATION_ID, builder.build())

        lifecycleScope.launch {
            homeViewModel.uploadFile(
                data,
                {
                    onUploadSuccessListener(it, builder, notificationManager)
                }
            ) { progress ->
                onUploadProgress(progress, builder, notificationManager)
            }
        }
    }

    private fun onUploadSuccessListener(
        uri: Task<Uri>,
        builder: NotificationCompat.Builder,
        notificationManager: NotificationManagerCompat
    ) {
        while (!uri.isComplete);
        val file = File(
            binding.tfFileName.editText?.text.toString(),
            binding.tfFileName.suffixText.toString(),
            binding.dpSubject.editText?.text.toString(),
            uri.result.toString(),
            "anonymous"
        )

        dbRef.push().key?.let { key -> dbRef.child(key).setValue(file) }

        builder.setProgress(0, 0, false)
        builder.setContentText("Upload complete")
        builder.priority = PRIORITY_DEFAULT
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun onUploadProgress(
        progress: Double,
        builder: NotificationCompat.Builder,
        notificationManager: NotificationManagerCompat
    ) {
        builder.priority = PRIORITY_LOW
        builder.setProgress(PROGRESS_MAX, progress.toInt(), false)
        builder.setContentText("${progress.toInt()}%")
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}