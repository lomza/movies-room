package com.lomza.moviesroom.movie

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lomza.moviesroom.db.Movie
import com.lomza.moviesroom.db.MovieDao
import com.lomza.moviesroom.db.MoviesDatabase

/**
 * @author Antonina
 */
class MoviesViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao: MovieDao = MoviesDatabase.getDatabase(application).movieDao()
    val moviesList: LiveData<List<Movie>>

    init {
        moviesList = movieDao.allMovies
    }

    fun insert(vararg movies: Movie?) {
        movieDao.insert(*movies)
    }

    fun update(movie: Movie?) {
        movieDao.update(movie)
    }

    fun deleteAll() {
        movieDao.deleteAll()
    }
}