
package com.example.tarea3.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tarea3.api.ApiService
import com.example.tarea3.api.RetrofitClient
import com.example.tarea3.model.Movie
import com.example.tarea3.model.MovieDetails
import com.example.tarea3.paging.MoviePagingSource
import com.example.tarea3.paging.SearchMoviePagingSource
import kotlinx.coroutines.flow.Flow

class MovieRepository {
    private val apiService = RetrofitClient.getMovieClient().create(ApiService::class.java)

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, RetrofitClient.API_KEY) }
        ).flow
    }

    fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchMoviePagingSource(apiService, RetrofitClient.API_KEY, query) }
        ).flow
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return apiService.getMovieDetails(movieId, RetrofitClient.API_KEY)
    }
}