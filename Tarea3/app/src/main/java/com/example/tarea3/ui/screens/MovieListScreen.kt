package com.example.tarea3.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tarea3.api.RetrofitClient.IMAGE_BASE_URL
import com.example.tarea3.model.Movie
import com.example.tarea3.viewmodel.MovieViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val movies = viewModel.getMovies().collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.searchMovies(it) },
            onClear = { viewModel.clearSearch() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        MovieGrid(
            movies = movies,
            onMovieClick = onMovieClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(query) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChange(it)
        },
        modifier = modifier,
        label = { Text("Buscar películas") },
        singleLine = true,
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onClear()
                }) {
                    Text("X")
                }
            }
        }
    )
}

@Composable
fun MovieGrid(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (movies.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (movies.loadState.refresh is LoadState.Error) {
            val error = (movies.loadState.refresh as LoadState.Error).error
            ErrorMessage(
                message = error.localizedMessage ?: "Error desconocido",
                onRetry = { movies.refresh() },
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (movies.itemCount == 0) {
            Text(
                text = "No se encontraron películas",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies.itemCount) { index ->
                    movies[index]?.let { movie ->
                        MovieItem(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                if (movies.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(2) }) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = if (movie.poster_path != null) "$IMAGE_BASE_URL${movie.poster_path}" else null,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f/3f)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = movie.release_date.take(4),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "${movie.vote_average}/10",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}