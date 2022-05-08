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
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val fileUseCases: FileUseCases
): ViewModel() {

    private val _viewState = MutableLiveData<String>().apply {
        value = LIST_VIEW
    }
    val viewState: LiveData<String> = _viewState

    private val _texGetFilesError = MutableLiveData<String>()
    val texGetFilesError: LiveData<String> = _texGetFilesError

    private val _files = MutableLiveData<List<File>>()
    val files: LiveData<List<File>> = _files

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    init {
        getFiles()
        getCourses()
    }

    fun setViewState(viewState: String) {
        _viewState.value = viewState
    }

    suspend fun uploadFile(uri: Uri): UploadTask.TaskSnapshot? {
         return fileUseCases.uploadFile(uri)
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
        viewModelScope.launch {
            try {
                _files.postValue(fileUseCases.getFiles())
            } catch (e: Exception) {
                _texGetFilesError.value = e.message
            }
        }
    }
}