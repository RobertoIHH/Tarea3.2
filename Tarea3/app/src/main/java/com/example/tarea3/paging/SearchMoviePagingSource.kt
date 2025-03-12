
package com.example.tarea3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tarea3.api.ApiService
import com.example.tarea3.model.Movie

class SearchMoviePagingSource(
    private val apiService: ApiService,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.searchMovies(apiKey, query, page)

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page == response.total_pages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}