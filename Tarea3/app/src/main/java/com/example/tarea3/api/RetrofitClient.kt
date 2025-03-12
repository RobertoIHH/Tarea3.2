package com.example.tarea3.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configura el cliente Retrofit para comunicarse con el backend
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"  // URL para emulador Android
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())  // Convierte JSON a objetos
                .build()
        }
        return retrofit!!
    }
}