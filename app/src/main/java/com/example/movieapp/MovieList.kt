package com.example.movieapp

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMovieListBinding

class MovieList : AppCompatActivity() {
    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MovieViewModel
    private lateinit var binding: ActivityMovieListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list)
        binding.lifecycleOwner = this

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        binding.viewModel = viewModel

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = MovieAdapter(emptyList())

        viewModel.moviesLiveData.observe(this) { movies ->
            adapter = MovieAdapter(movies)
            binding.recyclerView.adapter = adapter
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMovies(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.nowPlaying.setOnClickListener {
            binding.popular.setBackgroundResource(android.R.color.transparent)
            binding.popular.setTypeface(null)
            binding.nowPlaying.setBackgroundResource(R.drawable.btn_layout)
            binding.nowPlaying.setTypeface(Typeface.DEFAULT_BOLD)
            viewModel.getMoviesData("now_playing")
        }

        binding.popular.setOnClickListener {
            binding.nowPlaying.setBackgroundResource(android.R.color.transparent)
            binding.nowPlaying.setTypeface(null)
            binding.popular.setBackgroundResource(R.drawable.btn_layout)
            binding.popular.setTypeface(Typeface.DEFAULT_BOLD)
            viewModel.getMoviesData("popular")
        }
    }
}