package com.example.tarea3.api

import com.example.tarea3.model.HelloResponse
import com.example.tarea3.model.MovieDetails
import com.example.tarea3.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define los endpoints de la API.
 * Retrofit convierte estos métodos en llamadas HTTP.
 */
interface ApiService {
    /**
     * Endpoint para el backend local de ejemplo.
     * @return Una llamada que devolverá un objeto HelloResponse
     */
    @GET("hello")
    fun getHello(): Call<HelloResponse>

    /**
     * Obtiene una lista de películas populares.
     * @param apiKey La clave API necesaria para autenticar con TMDB
     * @param page El número de página para implementar paginación
     * @return Un objeto MovieResponse con la lista de películas
     */
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    /**
     * Obtiene los detalles de una película específica.
     * @param movieId El ID de la película a consultar
     * @param apiKey La clave API necesaria para autenticar con TMDB
     * @return Un objeto MovieDetails con información detallada
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): MovieDetails

    /**
     * Busca películas por un término de búsqueda.
     * @param apiKey La clave API necesaria para autenticar con TMDB
     * @param query El término de búsqueda
     * @param page El número de página para implementar paginación
     * @return Un objeto MovieResponse con los resultados de la búsqueda
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse
}