// ApiService.kt
package com.example.tarea3.api

import com.example.tarea3.model.HelloResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("hello")
    fun getHello(): Call<HelloResponse>
}

// RetrofitClient.kt
package com.example.tarea3.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

// HelloResponse.kt
package com.example.tarea3.model

data class HelloResponse(
    val message: String,
    val status: String
)