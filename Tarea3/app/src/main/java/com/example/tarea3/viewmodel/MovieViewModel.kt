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

/**
 * ViewModel que gestiona los datos de películas y su estado.
 * Implementa patrones de arquitectura MVVM (Model-View-ViewModel).
 */
class MovieViewModel : ViewModel() {
    // Repositorio que maneja las operaciones de datos
    private val repository = MovieRepository()

    // Estados observables para la UI
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

    // Flujos de datos para las películas
    private var _moviesFlow: Flow<PagingData<Movie>> = emptyFlow()
    private var _searchResultsFlow: Flow<PagingData<Movie>> = emptyFlow()

    /**
     * Inicializa el ViewModel cargando las películas populares.
     */
    init {
        // Carga las películas populares y las guarda en caché para mejor rendimiento
        _moviesFlow = repository.getPopularMovies().cachedIn(viewModelScope)
    }

    /**
     * Obtiene el flujo de películas actual según el estado de búsqueda.
     * @return Flujo de datos paginados de películas
     */
    fun getMovies(): Flow<PagingData<Movie>> {
        return if (_isSearching.value && _searchQuery.value.isNotEmpty()) {
            _searchResultsFlow  // Devuelve resultados de búsqueda
        } else {
            _moviesFlow  // Devuelve películas populares
        }
    }

    /**
     * Busca películas por un término de búsqueda.
     * @param query Término de búsqueda
     */
    fun searchMovies(query: String) {
        if (query.isEmpty()) {
            _isSearching.value = false
            return
        }

        _searchQuery.value = query
        _isSearching.value = true
        // Inicia una nueva búsqueda y guarda los resultados en caché
        _searchResultsFlow = repository.searchMovies(query).cachedIn(viewModelScope)
    }

    /**
     * Limpia la búsqueda actual y vuelve a mostrar películas populares.
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _isSearching.value = false
    }

    /**
     * Obtiene los detalles de una película específica.
     * @param movieId ID de la película
     */
    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                // Solicita detalles de la película al repositorio
                _movieDetails.value = repository.getMovieDetails(movieId)
            } catch (e: Exception) {
                // Maneja y expone errores
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia los detalles de la película actual.
     * Útil cuando se navega fuera de la pantalla de detalles.
     */
    fun clearMovieDetails() {
        _movieDetails.value = null
    }
}