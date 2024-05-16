package com.example.movieapp

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.movieapp.databinding.ActivityMovieDetailsBinding

class MovieDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMovieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)
        binding.lifecycleOwner = this

        displaysetup()

        val data = getIntents()

        val bitmap = BitmapFactory.decodeByteArray(data.byte, 0, data.byte?.size ?: 0)
        binding.movieImage.setImageBitmap(bitmap)
        binding.movieTitle.text = data.title
        binding.movieOverview.text = data.overview
        binding.movieRating.text = "${data.rating} / 10"
        binding.movieGenre.text = data.genre
        binding.movieReleaseDate.text = data.releaseDate
        binding.movieLanguage.text = data.language?.uppercase()
        binding.movieVoteCount.text = data.voteCount.toString()

        Handler(Looper.getMainLooper()).postDelayed({
                                                        if (binding.movieOverview.lineCount > 8) {
                                                            binding.movieOverview.maxLines = 8
                                                            binding.movieOverview.ellipsize = TextUtils.TruncateAt.END
                                                            binding.readMore.visibility = View.VISIBLE
                                                        } else {
                                                            binding.readMore.visibility = View.GONE
                                                        }
                                                    }, 50)

        binding.readMore.setOnClickListener {
            if (binding.readMore.text == "Read More") {
                binding.readMore.text = "Read Less"
                binding.movieOverview.maxLines = Integer.MAX_VALUE
            } else {
                binding.readMore.text = "Read More"
                binding.movieOverview.maxLines = 8
            }
        }

    }

    private fun getIntents() : Details_DataClass  {
        val byte = intent.getByteArrayExtra("movieImage")
        val title = intent.getStringExtra("movieTitle")
        val overview = intent.getStringExtra("movieOverview")
        val genre = intent.getStringExtra("movieGenres")
        val rating = intent.getStringExtra("movieRating")
        val releaseDate = intent.getStringExtra("movieReleaseDate")
        val language = intent.getStringExtra("movieLanguage")
        val voteCount = intent.getIntExtra("movieVoteCount", 0)

        return Details_DataClass(byte, title, overview, genre, rating, releaseDate, language, voteCount)
    }

    private fun displaysetup() {
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.show(WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}