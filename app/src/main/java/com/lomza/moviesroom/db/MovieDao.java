package com.lomza.moviesroom.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * @author Antonina
 */
@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie WHERE title = :title LIMIT 1")
    Movie findMovieByTitle(String title);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie... directors);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Movie director);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("SELECT * FROM movie ORDER BY title ASC")
    LiveData<List<Movie>> getAllMovies();
}
