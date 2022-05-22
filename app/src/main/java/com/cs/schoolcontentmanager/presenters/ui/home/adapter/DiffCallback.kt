package com.cs.schoolcontentmanager.presenters.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.cs.schoolcontentmanager.domain.model.File

object DiffCallback: DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean = oldItem == newItem
}