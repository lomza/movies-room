package com.lomza.moviesroom.director

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lomza.moviesroom.db.Director
import com.lomza.moviesroom.db.DirectorDao
import com.lomza.moviesroom.db.MoviesDatabase

/**
 * @author Antonina
 */
class DirectorsViewModel(application: Application) : AndroidViewModel(application) {

    private val directorDao: DirectorDao = MoviesDatabase.getDatabase(application).directorDao()
    val directorList: LiveData<List<Director>>

    init {
        directorList = directorDao.allDirectors
    }

    suspend fun insert(vararg directors: Director) {
        directorDao.insert(*directors)
    }

    suspend fun update(director: Director) {
        directorDao.update(director)
    }

    suspend fun deleteAll() {
        directorDao.deleteAll()
    }
}