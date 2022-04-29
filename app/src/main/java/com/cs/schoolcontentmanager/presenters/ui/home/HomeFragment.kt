package com.cs.schoolcontentmanager.presenters.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FragmentHomeBinding
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.FileAdapter
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.utils.Constants.GRID_VIEW
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.cs.schoolcontentmanager.utils.Util.isLandscape
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DatabaseReference
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

    private var fileAdapter = FileAdapter(mutableListOf())

    private val homeViewModel: HomeViewModel by activityViewModels()

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

        homeViewModel.files.observe(requireActivity()) {
            fileAdapter = FileAdapter(it)
            binding.rvFile.adapter = fileAdapter
        }

        homeViewModel.viewState.observe(requireActivity()) {
            when(it) {
                GRID_VIEW -> setGridViewState(it)
                LIST_VIEW -> {
                    rvFile.layoutManager = LinearLayoutManager(requireContext())
                    fileAdapter.setViewType(it)
                }
            }
        }

        homeViewModel.texGetFilesError.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
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

                searchView.queryHint = getString(R.string.file_name)

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

                setGridViewState(GRID_VIEW)
                homeViewModel.setViewState(GRID_VIEW)
                true
            }
            R.id.list_view -> {
                gridItem.isVisible = true
                listItem.isVisible = false

                binding.rvFile.layoutManager = LinearLayoutManager(requireContext())
                fileAdapter.setViewType(LIST_VIEW)
                homeViewModel.setViewState(LIST_VIEW)
                true
            }
            else -> {
                Toast.makeText(requireContext(), "Filter", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    private fun setGridViewState(viewType: String) {
        binding.rvFile.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        if (isLandscape(requireContext())) fileAdapter.setViewType(viewType, true)
        else fileAdapter.setViewType(viewType)
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