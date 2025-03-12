package com.example.tarea3.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // URL base para la API de TMDB
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    // URL para el backend local que se ejecuta en el emulador (10.0.2.2 es localhost del host)
    private const val LOCAL_URL = "http://10.0.2.2:8080/"

    // API Key necesaria para autenticar las solicitudes a TMDB
    const val API_KEY = "Api_Key" // Reemplaza con tu API Key real

    // URL base para las imágenes de TMDB
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    /**
     * Configura el cliente OkHttp con opciones avanzadas.
     * - Añade un interceptor de logging para depuración
     * - Establece tiempos de espera para conexión y lectura
     */
    private fun getOkHttpClient(): OkHttpClient {
        // Crea un interceptor para registrar las solicitudes y respuestas HTTP
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Construye y devuelve el cliente HTTP configurado
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para establecer conexión
            .readTimeout(30, TimeUnit.SECONDS)    // Tiempo máximo para leer datos
            .build()
    }

    /**
     * Crea un cliente Retrofit para consumir la API de TMDB.
     * Utiliza GsonConverterFactory para convertir automáticamente JSON a objetos Kotlin.
     */
    fun getMovieClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Crea un cliente Retrofit para consumir el backend local.
     * Útil para pruebas o APIs propias en desarrollo.
     */
    fun getLocalClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}