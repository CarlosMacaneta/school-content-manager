package com.cs.schoolcontentmanager.ui.home.bottomsheet.fragment.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.cs.schoolcontentmanager.domain.model.Course
import com.cs.schoolcontentmanager.domain.model.Subject

class SpinnerAdapter<T>(
    context: Context,
    @LayoutRes layout: Int,
    spinnerItems: MutableList<T>
): ArrayAdapter<T>(
    context,
    layout
) {
    private val items: MutableList<T> = mutableListOf()

    init {
        items.addAll(spinnerItems)
    }

    override fun add(`object`: T?) {
        `object`?.let {
            items.add(it)
        }
        notifyDataSetChanged()
    }

    override fun addAll(collection: MutableCollection<out T>) {
        collection.let {
            items.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun clear() {
        items.clear()
    }

    override fun getCount() = items.size

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            when(val item = items[position]) {
                is Course -> text = item.name
                is Subject -> text = item.name
            }
        }
    }

    override fun getItem(position: Int) = items[position]

    override fun getPosition(item: T?) = items.indexOf(item)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            when(val item = items[position]) {
                is Course -> text = item.name
                is Subject -> text = item.name
            }
        }
    }
}