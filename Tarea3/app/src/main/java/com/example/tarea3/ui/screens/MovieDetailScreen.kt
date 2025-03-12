package com.example.tarea3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tarea3.api.RetrofitClient.IMAGE_BASE_URL
import com.example.tarea3.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    onBackClick: () -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMovieDetails()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movieDetails?.title ?: "Detalles de la película"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("<")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            } else if (error != null) {
                ErrorMessage(
                    message = error ?: "Error desconocido",
                    onRetry = { movieId.let { viewModel.getMovieDetails(it) } },
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            } else {
                movieDetails?.let { movie ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = if (movie.backdrop_path != null)
                                "$IMAGE_BASE_URL${movie.backdrop_path}"
                            else if (movie.poster_path != null)
                                "$IMAGE_BASE_URL${movie.poster_path}"
                            else null,
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = movie.release_date.split("-")[0],
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "${movie.vote_average}/10",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Géneros: ${movie.genres.joinToString { it.name }}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (movie.runtime != null) {
                                Text(
                                    text = "Duración: ${movie.runtime} minutos",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Text(
                                text = "Estado: ${movie.status}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Sinopsis",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = movie.overview.ifEmpty { "No hay sinopsis disponible" },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}