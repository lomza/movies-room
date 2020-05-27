package com.lomza.moviesroom.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Antonina
 */
@Dao
interface MovieDao {

    @Query("SELECT * FROM movie WHERE title = :title LIMIT 1")
    suspend fun findMovieByTitle(title: String?): Movie?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg directors: Movie)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(director: Movie)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

    @get:Query("SELECT * FROM movie ORDER BY title ASC")
    val allMovies: LiveData<List<Movie>>
}