package com.cs.schoolcontentmanager.presenters.ui.home.viewmodel

import android.app.DownloadManager
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs.schoolcontentmanager.domain.model.Course
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.domain.usecase.FileUseCases
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup
import com.cs.schoolcontentmanager.presenters.ui.home.bottomsheet.util.FileSetup.fileExists
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.io.path.Path
import kotlin.io.path.exists

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val fileUseCases: FileUseCases
): ViewModel() {

    private val _viewState = MutableLiveData<String>().apply {
        value = LIST_VIEW
    }
    val viewState: LiveData<String> = _viewState

    private val _isFilterEnabled = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isFilterEnabled: LiveData<Boolean> = _isFilterEnabled

    private val _texGetFilesError = MutableLiveData<String>()
    val texGetFilesError: LiveData<String> = _texGetFilesError

    private val _files = MutableLiveData<List<File>>()
    val files: LiveData<List<File>> = _files

    private val _localFiles = MutableLiveData<List<File>>()
    val localFiles: LiveData<List<File>> = _localFiles

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private var getFilesJob: Job? = null

    init {
        getFiles()
        getLocalFiles()
        getCourses()
    }

    fun setViewState(viewState: String) {
        _viewState.value = viewState
    }

    fun setFilterEnabled(isFilterEnabled: Boolean) {
        _isFilterEnabled.value = isFilterEnabled
    }

    fun createFile(file: File) {
        viewModelScope.launch {
            fileUseCases.createFile(file)
        }
    }

    fun deleteFile(file: File) {
        viewModelScope.launch {
            fileUseCases.deleteFile(file)
        }
    }

    fun uploadFile(
        uri: Uri,
        onSuccessListener: (taskUri: Task<Uri>) -> Unit,
        onProgressListener: (progress: Double) -> Unit
    ) {
         fileUseCases.uploadFile(uri, onSuccessListener, onProgressListener)
    }

    private fun getCourses() {
        viewModelScope.launch {
            _courses.value = fileUseCases.getCourses()
        }
    }

    fun downloadFile(
        genFile: java.io.File,
        file: File,
        downloadManager: (downloadManagerRequest: DownloadManager.Request) -> Unit
    ) {
        viewModelScope.launch {
            val request = fileUseCases.downloadFile(file)
            request.setDestinationUri(Uri.fromFile(genFile))
            downloadManager(request)
        }
    }

    private fun getFiles() {
        try {
            fileUseCases.getFiles {
                _files.value = it
            }
        } catch (e: Exception) {
            _texGetFilesError.value = e.message
        }
    }

    fun searchFilesOnCloud(
        value: String,
        result: (files: MutableList<File>) -> Unit
    ) {
        fileUseCases.searchFiles(value, result)
    }

    fun searchLocalFileByName(
        name: String,
        result: (files: List<File>) -> Unit
    ) {
        getFilesJob?.cancel()
        getFilesJob = fileUseCases
            .searchLocalFileByName(String.format("%s%s%s", "%", name, "%"))
            .onEach {
                result(it)
            }
            .launchIn(viewModelScope)
    }

    private fun getLocalFiles() {
        getFilesJob?.cancel()
        getFilesJob = fileUseCases.getLocalFiles()
            .onEach {
                _localFiles.value = it.filter { item ->
                    java.io.File(item.uri).exists()
                }
            }
            .launchIn(viewModelScope)
    }
}