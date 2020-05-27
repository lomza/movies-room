package com.lomza.moviesroom.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun rePopulateDb(database: MoviesDatabase?) {
    database?.let { db ->
        withContext(Dispatchers.IO) {
            val movieDao: MovieDao = db.movieDao()
            val directorDao: DirectorDao = db.directorDao()

            movieDao.deleteAll()
            directorDao.deleteAll()

            val directorOne = Director(fullName = "Adam McKay")
            val directorTwo = Director(fullName = "Denis Villeneuve", age = 35)
            val directorThree = Director(fullName = "Morten Tyldum", age = 26)
            val movieOne = Movie(title = "The Big Short", directorId = directorDao.insert(directorOne))
            val dIdTwo = directorDao.insert(directorTwo)
            val movieTwo = Movie(title = "Arrival", directorId = dIdTwo)
            val movieThree = Movie(title = "Blade Runner 2049", directorId = dIdTwo)
            val movieFour = Movie(title = "Passengers", directorId = directorDao.insert(directorThree))
            movieDao.insert(movieOne, movieTwo, movieThree, movieFour)
        }
    }
}