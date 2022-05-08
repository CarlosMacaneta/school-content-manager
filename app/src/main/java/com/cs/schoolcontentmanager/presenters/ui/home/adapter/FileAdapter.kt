package com.cs.schoolcontentmanager.presenters.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cs.schoolcontentmanager.databinding.ItemHomeBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW

class FileAdapter(
    private val files: List<File>,
    viewType: String = LIST_VIEW
): RecyclerView.Adapter<FileViewHolder>() {

    private var type: String = ""
    private var isGridList = false

    private lateinit var callbackResult: ICallbackResult

    init {
        type = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FileViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position], type, isGridList, callbackResult)
    }

    override fun getItemCount() = files.size

    fun setViewType(type: String) {
        this.type = type
    }

    fun setViewType(type: String, isGridList: Boolean = false) {
        this.type = type
        this.isGridList = isGridList
    }

    fun setCallbackResult(callbackResult: ICallbackResult) {
        this.callbackResult = callbackResult
    }
}