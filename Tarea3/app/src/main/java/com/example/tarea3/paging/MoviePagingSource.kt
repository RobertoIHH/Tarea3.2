package com.example.tarea3.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tarea3.api.ApiService
import com.example.tarea3.model.Movie

/**
 * Fuente de datos para la paginación de películas populares.
 * Implementa PagingSource de la biblioteca de paginación de Android.
 *
 * @param apiService Servicio API para realizar las solicitudes
 * @param apiKey Clave API para autenticación
 */
class MoviePagingSource(
    private val apiService: ApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    /**
     * Determina qué página cargar cuando se actualiza la lista.
     * Usado para manejar actualizaciones y recrear el estado de paginación.
     */
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Intenta encontrar la página más cercana a la posición de anclaje
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    /**
     * Carga una página de datos.
     * Se llama automáticamente por la biblioteca de paginación cuando se necesitan más datos.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            // Si params.key es null, carga la primera página
            val page = params.key ?: 1
            // Realiza la solicitud a la API
            val response = apiService.getPopularMovies(apiKey, page)

            // Devuelve los resultados en un objeto LoadResult.Page
            LoadResult.Page(
                data = response.results,  // Lista de películas
                prevKey = if (page == 1) null else page - 1,  // Clave para cargar la página anterior (null si es primera página)
                nextKey = if (page == response.total_pages) null else page + 1  // Clave para cargar la página siguiente (null si es última página)
            )
        } catch (e: Exception) {
            // Maneja errores y los propaga a la UI
            LoadResult.Error(e)
        }
    }
}