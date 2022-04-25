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

    init {
        type = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FileViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(files[position], type)
    }

    override fun getItemCount() = files.size

    fun setViewType(type: String) {
        this.type = type
    }
}