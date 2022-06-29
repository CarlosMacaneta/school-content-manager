package com.cs.schoolcontentmanager.presenters.ui.home.download

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cs.schoolcontentmanager.BuildConfig
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FragmentFileManagerBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.FileAdapter
import com.cs.schoolcontentmanager.presenters.ui.home.adapter.ICallbackResult
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.fileSize
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.launchFile
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.mimeTypes
import com.cs.schoolcontentmanager.presenters.ui.home.filter.FilterBottomSheetFragment
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.presenters.ui.utils.Utility.intentDownload
import com.cs.schoolcontentmanager.utils.Constants
import com.cs.schoolcontentmanager.utils.Constants.DOC
import com.cs.schoolcontentmanager.utils.Constants.DOCX
import com.cs.schoolcontentmanager.utils.Constants.DOWNLOADED_FILES_VIEW
import com.cs.schoolcontentmanager.utils.Constants.EXCEL
import com.cs.schoolcontentmanager.utils.Constants.JPEG
import com.cs.schoolcontentmanager.utils.Constants.JPG
import com.cs.schoolcontentmanager.utils.Constants.PDF
import com.cs.schoolcontentmanager.utils.Constants.PNG
import com.cs.schoolcontentmanager.utils.Constants.POWER_POINT
import com.cs.schoolcontentmanager.utils.Constants.PPT
import com.cs.schoolcontentmanager.utils.Constants.PPTX
import com.cs.schoolcontentmanager.utils.Constants.WORD
import com.cs.schoolcontentmanager.utils.Constants.XLS
import com.cs.schoolcontentmanager.utils.Constants.XLSX
import com.cs.schoolcontentmanager.utils.Util
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.ui.PdfActivity
import dagger.hilt.android.AndroidEntryPoint
import okio.Okio
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class DownloadedFilesFragment : Fragment() {

    private lateinit var binding: FragmentFileManagerBinding
    private var fileAdapter = FileAdapter(displayView = DOWNLOADED_FILES_VIEW)
    private val homeViewModel: HomeViewModel by activityViewModels()

    @Inject lateinit var config: PdfActivityConfiguration
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
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                    when(file.type) {
                        PDF -> PdfActivity.showDocument(requireContext(), launchFile(requireContext(), file.uri), config)
                        PNG, JPEG, JPG -> {
                            try {
                                intent.setDataAndType(launchFile(requireContext(), file.uri), "image/*")
                                startActivity(intent)
                            } catch (e: Exception) {
                                Timber.tag("DFF").e(e.message)
                                Timber.tag("DFF").e(java.io.File(file.uri).name)
                                Toast.makeText(requireContext(), getString(R.string.unable_to_open_image), Toast.LENGTH_SHORT).show()
                            }
                        }
                        DOC -> {
                            try {
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes.first())
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(WORD))
                            }
                        }
                        DOCX -> {
                            try{
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes[1])
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(WORD))
                            }
                        }
                        PPT -> {
                            try{
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes[2])
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(POWER_POINT))
                            }
                        }
                        PPTX -> {
                            try{
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes[3])
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(POWER_POINT))
                            }
                        }
                        XLS -> {
                            try{
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes[4])
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(EXCEL))
                            }
                        }
                        XLSX -> {
                            try {
                                intent.setDataAndType(launchFile(requireContext(), file.uri), mimeTypes[5])
                                startActivity(intent)
                            } catch (e: Exception) {
                                startActivity(intentDownload(EXCEL))
                            }
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
                filter.show(requireActivity().supportFragmentManager, Constants.BS_FILTER)
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