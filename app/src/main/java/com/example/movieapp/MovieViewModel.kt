package com.example.movieapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movielibrary.FetchData
import com.example.movielibrary.Movie

class MovieViewModel : ViewModel() {
    private val _moviesLiveData = MutableLiveData<List<Movie>>()
    val moviesLiveData: LiveData<List<Movie>> = _moviesLiveData

    private val movielist = mutableListOf<Movie>()

    init {
        getMoviesData("now_playing")
    }

    fun getMoviesData(category: String) {
        val key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NTZiNjBlMzU3YmJjN2NkZWU5YTc4OGYzYTM0YTMzYyIsInN1YiI6IjY2NDBlNzJkZGFkOGY2ZjU2ODIwODQzMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.nm0EvkxRad_McJnHr3S5oPyOHSpkCriyhHhqdEemUAg"

        FetchData.getMovies(key, category, onSuccess = { movies ->
            if (movies.isNotEmpty()) {
                movielist.addAll(movies)
                _moviesLiveData.value = movielist.toList()
            } else {
                Log.e("API Error", "API response is empty")
            }
        }, onFailure = { error ->
            Log.e("API Error", "API request failed: $error")
        })
    }

    fun filterMovies(query: String) {
        val filteredList = movielist.filter { it.title.lowercase().contains(query.lowercase()) || it.overview.lowercase().contains(query.lowercase()) }
        _moviesLiveData.value = filteredList
    }
}