package com.cs.schoolcontentmanager.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.cs.schoolcontentmanager.databinding.ItemHomeBinding
import com.cs.schoolcontentmanager.domain.model.File

class FileViewHolder(
    private val binding: ItemHomeBinding
): RecyclerView.ViewHolder(binding.root) {

    private val fileName = binding.fileName

    fun bind(file: File) {
        fileName.text = file.name


    }
}