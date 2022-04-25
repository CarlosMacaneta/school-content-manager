package com.cs.schoolcontentmanager.presenters.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FragmentHomeBinding
import com.cs.schoolcontentmanager.domain.model.File
<<<<<<< HEAD:app/src/main/java/com/cs/schoolcontentmanager/presenters/ui/home/HomeFragment.kt
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.FileAdapter
=======
import com.cs.schoolcontentmanager.ui.home.adapter.FileAdapter
import com.cs.schoolcontentmanager.utils.Constants.GRID_VIEW
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
>>>>>>> 5a8014da2a64a77760a50238017f34e85dc7a6a6:app/src/main/java/com/cs/schoolcontentmanager/ui/home/HomeFragment.kt
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

    private lateinit var fileAdapter: FileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
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
        dbRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val files = mutableListOf<File>()

                snapshot.children.forEach {
                    it.getValue(File::class.java)?.let { file -> files.add(file) }
                }
                fileAdapter = FileAdapter(files)
                binding.rvFile.adapter = fileAdapter.apply {
                    notifyItemChanged(0, itemCount.dec())
                }
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

    private lateinit var topMenu: Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        topMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val gridItem = topMenu.findItem(R.id.grid_view)
        val listItem = topMenu.findItem(R.id.list_view)

        return when(item.itemId) {
            R.id.search -> {
                val searchView = item.actionView as SearchView

                searchView.isIconified = true

                searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        Toast.makeText(requireContext(), p0.toString(), Toast.LENGTH_SHORT).show()
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        Log.e("jk", "onQueryTextChange: $p0", )
                        return true
                    }
                })
                true
            }
            R.id.grid_view -> {
                gridItem.isVisible = false
                listItem.isVisible = true

                binding.rvFile.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                fileAdapter.setViewType(GRID_VIEW)
                true
            }
            R.id.list_view -> {
                gridItem.isVisible = true
                listItem.isVisible = false

                binding.rvFile.layoutManager = LinearLayoutManager(requireContext())
                fileAdapter.setViewType(LIST_VIEW)
                true
            }
            else -> {
                Toast.makeText(requireContext(), "Filter", Toast.LENGTH_SHORT).show()
                true
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