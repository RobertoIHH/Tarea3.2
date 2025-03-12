
package com.example.tarea3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tarea3.model.Movie
import com.example.tarea3.model.MovieDetails
import com.example.tarea3.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> = _movieDetails

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _moviesFlow: Flow<PagingData<Movie>> = emptyFlow()
    private var _searchResultsFlow: Flow<PagingData<Movie>> = emptyFlow()

    init {
        _moviesFlow = repository.getPopularMovies().cachedIn(viewModelScope)
    }

    fun getMovies(): Flow<PagingData<Movie>> {
        return if (_isSearching.value && _searchQuery.value.isNotEmpty()) {
            _searchResultsFlow
        } else {
            _moviesFlow
        }
    }

    fun searchMovies(query: String) {
        if (query.isEmpty()) {
            _isSearching.value = false
            return
        }

        _searchQuery.value = query
        _isSearching.value = true
        _searchResultsFlow = repository.searchMovies(query).cachedIn(viewModelScope)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _isSearching.value = false
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _movieDetails.value = repository.getMovieDetails(movieId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMovieDetails() {
        _movieDetails.value = null
    }
}