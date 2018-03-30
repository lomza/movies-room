package com.lomza.moviesroom.director;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.lomza.moviesroom.db.Director;
import com.lomza.moviesroom.db.DirectorDao;
import com.lomza.moviesroom.db.MoviesDatabase;

import java.util.List;

/**
 * @author Antonina
 */

public class DirectorsViewModel extends AndroidViewModel {
    private DirectorDao directorDao;
    private LiveData<List<Director>> directorsLiveData;

    public DirectorsViewModel(@NonNull Application application) {
        super(application);
        directorDao = MoviesDatabase.getDatabase(application).directorDao();
        directorsLiveData = directorDao.getAllDirectors();
    }

    public LiveData<List<Director>> getDirectorList() {
        return directorsLiveData;
    }

    public void insert(Director... directors) {
        directorDao.insert(directors);
    }

    public void update(Director director) {
        directorDao.update(director);
    }

    public void deleteAll() {
        directorDao.deleteAll();
    }
}