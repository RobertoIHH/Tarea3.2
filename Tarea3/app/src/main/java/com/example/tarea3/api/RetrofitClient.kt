package com.example.tarea3.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    private const val LOCAL_URL = "http://10.0.2.2:8080/"

    // Reemplaza esto con tu API Key
    const val API_KEY = "tu_api_key_aquí"

    // URL base para imágenes
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun getMovieClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getLocalClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCAL_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}