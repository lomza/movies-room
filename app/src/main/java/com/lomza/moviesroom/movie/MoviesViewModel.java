package com.lomza.moviesroom.movie;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.lomza.moviesroom.db.Movie;
import com.lomza.moviesroom.db.MovieDao;
import com.lomza.moviesroom.db.MoviesDatabase;

import java.util.List;

/**
 * @author Antonina
 */

public class MoviesViewModel extends AndroidViewModel {
    private MovieDao movieDao;
    private LiveData<List<Movie>> moviesLiveData;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        movieDao = MoviesDatabase.getDatabase(application).movieDao();
        moviesLiveData = movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getMoviesList() {
        return moviesLiveData;
    }

    public void insert(Movie... movies) {
        movieDao.insert(movies);
    }

    public void update(Movie movie) {
        movieDao.update(movie);
    }

    public void deleteAll() {
        movieDao.deleteAll();
    }
}