package com.cs.schoolcontentmanager.presenters.ui.home.download

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.mimeTypes
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.utils.Constants
import com.cs.schoolcontentmanager.utils.Constants.DOC
import com.cs.schoolcontentmanager.utils.Constants.DOCX
import com.cs.schoolcontentmanager.utils.Constants.DOWNLOADED_FILES_VIEW
import com.cs.schoolcontentmanager.utils.Constants.JPEG
import com.cs.schoolcontentmanager.utils.Constants.PDF
import com.cs.schoolcontentmanager.utils.Constants.PNG
import com.cs.schoolcontentmanager.utils.Constants.PPT
import com.cs.schoolcontentmanager.utils.Constants.PPTX
import com.cs.schoolcontentmanager.utils.Constants.XLS
import com.cs.schoolcontentmanager.utils.Constants.XLSX
import com.cs.schoolcontentmanager.utils.Util
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.ui.PdfActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DownloadedFilesFragment : Fragment() {

    private lateinit var binding: FragmentFileManagerBinding
    private var fileAdapter = FileAdapter(displayView = DOWNLOADED_FILES_VIEW)
    private val homeViewModel: HomeViewModel by activityViewModels()

    @Inject lateinit var config: PdfActivityConfiguration

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
    }

    private fun initRecyclerView() {
        val rvFile = binding.rvFile
        rvFile.setHasFixedSize(true)

        homeViewModel.localFiles.observe(requireActivity()) {
            fileAdapter.submitData(it)
            binding.rvFile.adapter = fileAdapter

            fileAdapter.setCallbackResult(object : ICallbackResult {
                override fun getResultCallback(itemView: View, position: Int, file: File) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW

                    when(file.type) {
                        PDF -> PdfActivity.showDocument(requireContext(), Uri.parse(file.uri), config)
                        PNG, JPEG -> {
                            intent.setDataAndType(Uri.parse(file.uri), "image/*")
                            startActivity(intent)
                        }
                        DOC -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes.first())
                            startActivity(intent)
                        }
                        DOCX -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes[1])
                            startActivity(intent)
                        }
                        PPT -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes[2])
                            startActivity(intent)
                        }
                        PPTX -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes[3])
                            startActivity(intent)
                        }
                        XLS -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes[4])
                            startActivity(intent)
                        }
                        XLSX -> {
                            intent.setDataAndType(Uri.parse(file.uri), mimeTypes[5])
                            startActivity(intent)
                        }
                    }
                }
            })
        }

        homeViewModel.viewState.observe(requireActivity()) {
            when(it) {
                Constants.GRID_VIEW -> setGridViewState(it)
                Constants.LIST_VIEW -> {
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
                searchView.isIconified = true

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        Toast.makeText(requireContext(), p0.toString(), Toast.LENGTH_SHORT).show()
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        p0?.let {
                            if (it.isNotBlank() && it.isNotEmpty()) {
                                homeViewModel.searchLocalFileByName(it) { files ->
                                    fileAdapter.submitData(files)
                                }
                            } else {
                                homeViewModel.localFiles.observe(viewLifecycleOwner) { files ->
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

                setGridViewState(Constants.GRID_VIEW)
                homeViewModel.setViewState(Constants.GRID_VIEW)
                true
            }
            R.id.list_view -> {
                gridItem.isVisible = true
                listItem.isVisible = false

                binding.rvFile.layoutManager = LinearLayoutManager(requireContext())
                fileAdapter.setViewType(Constants.LIST_VIEW)
                homeViewModel.setViewState(Constants.LIST_VIEW)
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

        if (Util.isLandscape(requireContext())) fileAdapter.setViewType(viewType, true)
        else fileAdapter.setViewType(viewType)
    }
}