package com.cs.schoolcontentmanager.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val dbRef: DatabaseReference
): ViewModel() {

    private val _viewState = MutableLiveData<String>().apply {
        value = LIST_VIEW
    }
    val viewState: LiveData<String> = _viewState

    private val _texGetFilesError = MutableLiveData<String>()
    val texGetFilesError: LiveData<String> = _texGetFilesError

    private val _files = MutableLiveData<List<File>>()
    val files: LiveData<List<File>> = _files

    init {
        getFiles()
    }

    fun setViewState(viewState: String) {
        _viewState.value = viewState
    }

    private fun getFiles() {
        dbRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<File>()

                snapshot.children.forEach {
                    it.getValue(File::class.java)?.let { file -> temp.add(file) }
                }
                _files.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
                _texGetFilesError.value = "Error fetching data from the server, " +
                        "make sure that you are connected to internet..."
            }
        })
    }
}