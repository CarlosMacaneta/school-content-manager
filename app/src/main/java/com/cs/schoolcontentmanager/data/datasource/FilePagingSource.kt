//package com.cs.schoolcontentmanager.data.datasource
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.cs.schoolcontentmanager.domain.model.File
//import com.cs.schoolcontentmanager.domain.repository.FileRepository
//import timber.log.Timber
//
//class FilePagingSource(
//     private val repository: FileRepository
// ): PagingSource<Int, File>() {
//    override fun getRefreshKey(state: PagingState<Int, File>): Int? =
//        null
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, File> {
//        val nextPage = params.key ?: FIRST_PAGE_INDEX
//        val files = repository. getAllFiles(params.loadSize)
//        Timber.e("$files")
//
//        return LoadResult.Page(
//            data = files,
//            prevKey = if (nextPage == FIRST_PAGE_INDEX) null else nextPage - 1,
//            nextKey = if (files.isNullOrEmpty()) null else nextPage + 1
//        )
//    }
//
//    companion object {
//        const val FIRST_PAGE_INDEX = 0
//    }
//}