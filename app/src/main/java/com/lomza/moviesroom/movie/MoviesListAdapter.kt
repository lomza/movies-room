package com.lomza.moviesroom.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lomza.moviesroom.R
import com.lomza.moviesroom.db.Movie
import com.lomza.moviesroom.db.MoviesDatabase
import com.lomza.moviesroom.movie.MovieSaveDialogFragment.Companion.newInstance
import com.lomza.moviesroom.movie.MoviesListAdapter.MoviesViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @author Antonina
 */
class MoviesListAdapter(private val parent: Fragment) : RecyclerView.Adapter<MoviesViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(parent.requireContext())
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
        movieList?.let { list ->
            val movie = list[position]
            holder.titleText.text = movie.title
            runBlocking {
                val directorFullName = withContext(Dispatchers.Default) {
                    getDirectorFullName(movie)
                }

                holder.directorText.text = directorFullName ?: ""

                holder.itemView.setOnClickListener {
                    val dialogFragment: DialogFragment = newInstance(movie.title, directorFullName)
                    dialogFragment.setTargetFragment(parent, 99)
                    dialogFragment.show(
                        (parent.activity as AppCompatActivity).supportFragmentManager,
                        MovieSaveDialogFragment.TAG_DIALOG_MOVIE_SAVE
                    )
                }
            }
        }
    }

    private suspend fun getDirectorFullName(movie: Movie): String? {
        return MoviesDatabase.getDatabase(parent.requireContext()).directorDao().findDirectorById(movie.directorId)?.fullName
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
