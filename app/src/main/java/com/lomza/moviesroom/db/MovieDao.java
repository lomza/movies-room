package com.lomza.moviesroom.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
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
