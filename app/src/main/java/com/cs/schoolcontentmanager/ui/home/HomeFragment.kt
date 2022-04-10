package com.cs.schoolcontentmanager.ui.home

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
import com.cs.schoolcontentmanager.ui.home.adapter.FileAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

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

        dbRef.addValueEventListener(object : ValueEventListener {
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