package com.cs.schoolcontentmanager.presenters.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FragmentHomeBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.FileAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject lateinit var storageRef: StorageReference
    @Inject lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        navigateOut()
    }

    private fun initRecyclerView() {
        val rvFile = binding.rvFile

        rvFile.setHasFixedSize(true)
        rvFile.layoutManager = LinearLayoutManager(requireContext())
        getFiles()
    }

    private fun getFiles() {
        val files = ArrayList<File>()

        dbRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it.getValue(File::class.java)?.let { file -> files.add(file) }
                }

                binding.rvFile.adapter = FileAdapter(files)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error fetching data from the server, " +
                        "make sure that you are connected to internet...", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun downLoadFiles() {

    }

    private fun downloadFile(fileUrl: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val sRef = storageRef.child(fileUrl).downloadUrl.await()


        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "This download couldn't be finished.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateOut() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.exit))
                        .setMessage(getString(R.string.want_exit))
                        .setNegativeButton(getString(R.string.no)) { _, _ -> }
                        .setPositiveButton(getString(R.string.yes)) { _, _ -> requireActivity().finish() }
                        .show()
                }
            })
    }
}