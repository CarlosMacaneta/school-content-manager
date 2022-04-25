package com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.cs.schoolcontentmanager.databinding.BottomSheetOptionsDialogBinding
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.fragment.FileDetailsFragment
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.fragment.ScanFragment
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.extension
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.fileName
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.fileSize
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.intentFileChooser
import com.cs.schoolcontentmanager.utils.Util.launchFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class ModalBottomSheetOptions @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetOptionsDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BottomSheetOptionsDialogBinding.inflate(layoutInflater)

        val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                result?.data?.data?.let { uri ->
                    Toast.makeText(requireContext(), fileSize(requireContext(), uri)+", "+
                        extension(requireContext(), uri), Toast.LENGTH_LONG).show()
                    launchFragment(requireContext(), FileDetailsFragment(), uri,
                        fileName(uri, requireContext())
                    ).also { dismiss() }
                }
            }
        }

        binding.btnUpload.setOnClickListener {
            launcher.launch(intentFileChooser())
        }

        binding.btnScan.setOnClickListener {
            launchFragment(requireContext(), ScanFragment()).also { dismiss() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

}