package com.cs.schoolcontentmanager.presenters.ui.home

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FragmentFileManagerBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.FileAdapter
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.ICallbackResult
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup
import com.cs.schoolcontentmanager.presenters.ui.home.filter.FilterBottomSheetFragment
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.utils.Constants.BS_FILTER
import com.cs.schoolcontentmanager.utils.Constants.FOLDER_FILES
import com.cs.schoolcontentmanager.utils.Constants.GRID_VIEW
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.cs.schoolcontentmanager.utils.Util.isDarkMode
import com.cs.schoolcontentmanager.utils.Util.isLandscape
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentFileManagerBinding

    private var fileAdapter = FileAdapter()
    private val homeViewModel: HomeViewModel by activityViewModels()

    @Inject lateinit var filter: FilterBottomSheetFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFileManagerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        navigateOut()

        Toast.makeText(requireContext(), isDarkMode(requireContext()).toString(), Toast.LENGTH_SHORT).show()
    }

    private fun initRecyclerView() {
        val rvFile = binding.rvFile
        rvFile.setHasFixedSize(true)

        homeViewModel.files.observe(requireActivity()) {
            fileAdapter.submitData(it)
            binding.rvFile.adapter = fileAdapter
            fileAdapter.setCallbackResult(object : ICallbackResult {
                override fun getResultCallback(itemView: View, position: Int, file: File) {
                    downloadFile(file)
                }
            })
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
                searchView.isIconified = false
                val searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
                searchEditText.setTextColor(Color.WHITE)
                searchEditText.setHintTextColor(Color.WHITE)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        p0?.let {
                            if (it.isNotBlank() && it.isNotEmpty()) {
                                homeViewModel.searchFilesOnCloud(it) { files ->
                                    fileAdapter.submitData(files)
                                }
                            } else {
                                homeViewModel.files.observe(viewLifecycleOwner) { files ->
                                    fileAdapter.submitData(files)
                                }
                            }
                        }
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
                filter.show(requireActivity().supportFragmentManager, BS_FILTER)
                true
            }
        }
    }

    private fun setGridViewState(viewType: String) {
        binding.rvFile.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        if (isLandscape(requireContext())) fileAdapter.setViewType(viewType, true)
        else fileAdapter.setViewType(viewType)
    }

    private fun downloadFile(file: File) {
        val dir = FileSetup.getOutputDirectory(requireContext(), FOLDER_FILES)
        val genFile = java.io.File(dir, "${file.name}${file.type}")

        homeViewModel.downloadFile(genFile, file) {
            val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(it)

            Timber.e("Path: ${genFile.path}, \nCanonicalPath: ${genFile.canonicalPath}, \nAbsolutePath: ${genFile.absolutePath}")

            homeViewModel.createFile(File(
                file.name, file.type, file.subject, genFile.path,
                file.publishedBy
            ))
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