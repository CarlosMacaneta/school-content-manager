package com.cs.schoolcontentmanager.presenters.ui.home.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.databinding.FilterBottomSheetBinding
import com.cs.schoolcontentmanager.presenters.ui.home.viewmodel.HomeViewModel
import com.cs.schoolcontentmanager.presenters.utils.SpinnerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FilterBottomSheetFragment @Inject constructor(): BottomSheetDialogFragment() {

    private lateinit var binding: FilterBottomSheetBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FilterBottomSheetBinding.inflate(layoutInflater)

        loadDropdownData()
    }

    private fun loadDropdownData() {
        homeViewModel.courses.observe(requireActivity()) {
            val adapter = SpinnerAdapter(
                requireContext(),
                R.layout.spinner_item,
                it.toMutableList()
            )
            binding.courseItem.setAdapter(adapter)
            binding.courseItem.setOnItemClickListener { _, _, position, _ ->
                val a = SpinnerAdapter(
                    requireContext(),
                    R.layout.spinner_item,
                    it[position].subjects.toMutableList()
                )
                binding.subjectItem.setAdapter(a)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
}