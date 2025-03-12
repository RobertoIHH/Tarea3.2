package com.example.tarea3.model

/**
 * Modelo que representa una película básica.
 * Corresponde a los datos devueltos por la API de TMDB.
 */
data class Movie(
    val id: Int,                // ID único de la película
    val title: String,          // Título de la película
    val overview: String,       // Descripción/sinopsis
    val poster_path: String?,   // URL parcial del póster (puede ser nulo)
    val backdrop_path: String?, // URL parcial de la imagen de fondo (puede ser nulo)
    val vote_average: Double,   // Puntuación media (0-10)
    val release_date: String    // Fecha de lanzamiento en formato "YYYY-MM-DD"
)

/**
 * Modelo que representa la respuesta paginada de películas.
 * Utilizado para las listas de películas populares y resultados de búsqueda.
 */
data class MovieResponse(
    val page: Int,              // Número de página actual
    val results: List<Movie>,   // Lista de películas en esta página
    val total_pages: Int,       // Número total de páginas disponibles
    val total_results: Int      // Número total de resultados
)

/**
 * Modelo que representa los detalles completos de una película.
 * Contiene información más detallada que el modelo Movie básico.
 */
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val release_date: String,
    val genres: List<Genre>,    // Lista de géneros de la película
    val runtime: Int?,          // Duración en minutos (puede ser nulo)
    val status: String          // Estado (ej: "Released", "In Production")
)

/**
 * Modelo que representa un género de película.
 */
data class Genre(
    val id: Int,                // ID del género
    val name: String            // Nombre del género (ej: "Action", "Drama")
)