package com.example.tarea3.api

import com.example.tarea3.model.HelloResponse
import retrofit2.Call
import retrofit2.http.GET

// Interface que define los endpoints disponibles
interface ApiService {
    @GET("hello")  // Endpoint para obtener el mensaje
    fun getHello(): Call<HelloResponse>
}