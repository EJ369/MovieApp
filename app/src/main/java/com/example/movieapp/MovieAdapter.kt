package com.example.movieapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movielibrary.Movie
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

class MovieAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.bind(movie)
    }

    private fun fetchImage(posterPath: String?): Bitmap? {
        var connection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            connection = URL(posterPath).openConnection() as HttpURLConnection
            connection.connect()
            inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
            inputStream?.close()
        }
        return null
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moreInfo: Button = itemView.findViewById(R.id.moreInfo)
        val poster: ImageView = itemView.findViewById(R.id.moviePoster)
        val title: TextView = itemView.findViewById(R.id.movieTitle)
        val genre: TextView = itemView.findViewById(R.id.movieGenre)
        val rating: TextView = itemView.findViewById(R.id.movieRating)
        val voteCount: TextView = itemView.findViewById(R.id.voteCount)

        fun bind(movie: Movie) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            var bitmap: Bitmap? = null
            executor.execute {
                val image = fetchImage("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                handler.post {
                    poster.setImageBitmap(image)
                    if (image != null) {
                        bitmap = image
                    }
                }
            }

            title.text = movie.title

            val date = movie.releaseDate
            var newdate = ""
            try {
                val olddate = SimpleDateFormat("yyyy-MM-dd").parse(date)
                newdate = olddate?.let { SimpleDateFormat("dd MMM, yyyy").format(it) }.toString()
                //holder.date.text = newdate
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val genreslist = movie.genreIds
            val mappedGenres = genreslist.mapNotNull { Genres.mapIdToName(it) }
            val genres = mappedGenres.joinToString(", ")

            genre.text = genres

            val mrating = DecimalFormat("#.#").format(movie.voteAverage)
            rating.text = "$mrating / 10"

            voteCount.text = movie.voteCount.toString()

            moreInfo.setOnClickListener {
                val stream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                val intent = Intent(itemView.context, MovieDetails::class.java)
                intent.putExtra("movieImage", byteArray)
                intent.putExtra("movieTitle", movie.title)
                intent.putExtra("movieOverview", movie.overview)
                intent.putExtra("movieReleaseDate", newdate)
                intent.putExtra("movieGenres", genres)
                intent.putExtra("movieRating", mrating)
                intent.putExtra("movieLanguage", movie.originalLanguage)
                intent.putExtra("movieVoteCount", movie.voteCount)
                itemView.context.startActivity(intent)
            }
        }
    }
}