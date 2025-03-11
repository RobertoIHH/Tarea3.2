package com.example.tarea3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarea3.api.ApiService
import com.example.tarea3.api.RetrofitClient
import com.example.tarea3.model.HelloResponse
import com.example.tarea3.ui.theme.Tarea3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tarea3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HelloScreen()
                }
            }
        }
    }
}

@Composable
fun HelloScreen() {
    var message by remember { mutableStateOf("Esperando respuesta...") }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando...",
                fontSize = 18.sp
            )
        } else {
            Text(
                text = message,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true
                    isError = false
                    message = "Cargando..."

                    val apiService = RetrofitClient.getClient().create(ApiService::class.java)
                    val call = apiService.getHello()

                    call.enqueue(object : Callback<HelloResponse> {
                        override fun onResponse(call: Call<HelloResponse>, response: Response<HelloResponse>) {
                            isLoading = false

                            if (response.isSuccessful && response.body() != null) {
                                val helloResponse = response.body()!!
                                message = "Mensaje: ${helloResponse.message}\nEstado: ${helloResponse.status}"
                            } else {
                                isError = true
                                message = "Error en la respuesta: ${response.code()}"
                            }
                        }

                        override fun onFailure(call: Call<HelloResponse>, t: Throwable) {
                            isLoading = false
                            isError = true
                            message = "Error de conexión: ${t.message}"
                            t.printStackTrace()
                        }
                    })
                }
            ) {
                Text("Obtener Mensaje")
            }
        }
    }
}