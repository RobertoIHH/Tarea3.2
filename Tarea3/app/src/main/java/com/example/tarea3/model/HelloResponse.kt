package com.example.tarea3.model
// Clase de datos que mapea la respuesta JSON del servidor
data class HelloResponse(
    val message: String,  // Campo para el mensaje
    val status: String    // Campo para el estado
)