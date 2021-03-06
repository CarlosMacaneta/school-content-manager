package com.cs.schoolcontentmanager.presenters.ui.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.ItemHomeBinding
import com.cs.schoolcontentmanager.domain.model.File
import com.cs.schoolcontentmanager.utils.Constants.DOWNLOADED_FILES_VIEW
import com.cs.schoolcontentmanager.utils.Constants.GRID_VIEW
import com.cs.schoolcontentmanager.utils.Constants.LIST_VIEW
import com.cs.schoolcontentmanager.utils.Util.isDarkMode

class FileViewHolder(
    private val binding: ItemHomeBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        file: File,
        viewType: String,
        isGridList: Boolean,
        callbackResult: ICallbackResult?,
        displayView: String
    ) {
        decorateOnDarkMode()
        if (callbackResult == null) {
            binding.download.visibility = View.GONE
        } else {
            binding.download.visibility = View.VISIBLE
            binding.download.setOnClickListener {
                callbackResult.getResultCallback(itemView, absoluteAdapterPosition, file)
            }
        }

        if (displayView == DOWNLOADED_FILES_VIEW) {
            binding.download.visibility = View.GONE
            itemView.setOnClickListener {
                callbackResult?.getResultCallback(itemView, absoluteAdapterPosition, file)
            }
        } else binding.download.visibility = View.VISIBLE

        when (viewType) {
            LIST_VIEW -> listView(file)
            GRID_VIEW -> {
                if (isGridList) listView(file)
                else {
                    binding.listView.visibility = View.GONE
                    binding.gridView.root.visibility = View.VISIBLE

                    binding.gridView.fileName.text = String.format("%s%s", file.name, file.type)
                    binding.gridView.fileSubject.text = file.subject

                    when (file.type) {
                        ".pdf" -> binding.gridView.image.setImageResource(R.drawable.pdf_100)
                        ".ppt", ".pptx" -> binding.gridView.image.setImageResource(R.drawable.powerpoint_144)
                        ".xls", ".xlsx" -> binding.gridView.image.setImageResource(R.drawable.excel_144)
                        ".doc", ".docx" -> binding.gridView.image.setImageResource(R.drawable.word_144)
                        ".jpg", ".jpeg" -> binding.gridView.image.setImageResource(R.drawable.jpg_100)
                        ".png" -> binding.gridView.image.setImageResource(R.drawable.png_100)
                    }
                }
            }
        }
    }

    private fun listView(file: File) {
        binding.listView.visibility = View.VISIBLE
        binding.gridView.root.visibility = View.GONE

        binding.fileName.text = String.format("%s%s", file.name, file.type)
        binding.fileSubject.text = file.subject

        when (file.type) {
            ".pdf" -> binding.logo.setImageResource(R.drawable.pdf_32)
            ".ppt", ".pptx" -> binding.logo.setImageResource(R.drawable.powerpoint_32)
            ".xls", ".xlsx" -> binding.logo.setImageResource(R.drawable.xls_32)
            ".doc", ".docx" -> binding.logo.setImageResource(R.drawable.word_32)
            ".jpg", ".jpeg" -> binding.logo.setImageResource(R.drawable.jpg_32)
            ".png" -> binding.logo.setImageResource(R.drawable.png_32)
        }
    }

    private fun decorateOnDarkMode() {
        if (isDarkMode(itemView.context)) {
            binding.divider.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.dividerColor))
            binding.gridView.root.strokeColor = ContextCompat.getColor(itemView.context, R.color.dividerColor)
            binding.gridView.content.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.contentColor_2))
            binding.gridView.fileName.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        } else {
            binding.divider.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.grey_5))
            binding.gridView.root.strokeColor = ContextCompat.getColor(itemView.context, R.color.grey_20)
            binding.gridView.content.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            binding.gridView.fileName.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
        }
    }
}