package ru.crazerr.avitotest.utils.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: INITIAL_PAGE_NUMBER
        val pageSize = params.loadSize

        return try {
            val result = loadPage(page = page, pageSize = pageSize)

            result.fold(
                onSuccess = {
                    LoadResult.Page(
                        data = it,
                        prevKey = if (page == INITIAL_PAGE_NUMBER) null else page - 1,
                        nextKey = if (it.isEmpty()) null else page + 1
                    )
                },
                onFailure = { LoadResult.Error(it) }
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    protected abstract suspend fun loadPage(page: Int, pageSize: Int): Result<List<T>>

    companion object {
        private const val INITIAL_PAGE_NUMBER = 1
    }
}