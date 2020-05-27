package com.lomza.moviesroom.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Movie
import com.lomza.moviesroom.db.MoviesDatabase
import com.lomza.moviesroom.movie.MovieSaveDialogFragment.Companion.newInstance
import com.lomza.moviesroom.movie.MoviesListAdapter.MoviesViewHolder

/**
 * @author Antonina
 */
class MoviesListAdapter(val context: Context) : RecyclerView.Adapter<MoviesViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var movieList: List<Movie>? = null

    fun setMovieList(movieList: List<Movie>?) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_list_movie, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        movieList?.let {
            val movie = it[position]
            holder.titleText.text = movie.title
            val director = MoviesDatabase.getDatabase(context).directorDao().findDirectorById(movie.directorId)
            val directorFullName: String
            if (director != null) {
                holder.directorText.text = director.fullName
                directorFullName = director.fullName
            } else {
                directorFullName = ""
            }
            holder.itemView.setOnClickListener { v: View? ->
                val dialogFragment: DialogFragment = newInstance(movie.title, directorFullName)
                dialogFragment.show(
                    (context as AppCompatActivity).supportFragmentManager,
                    MovieSaveDialogFragment.TAG_DIALOG_MOVIE_SAVE
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return if (movieList == null) {
            0
        } else {
            movieList!!.size
        }
    }

    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tvMovieTitle)
        val directorText: TextView = itemView.findViewById(R.id.tvMovieDirectorFullName)
    }
}