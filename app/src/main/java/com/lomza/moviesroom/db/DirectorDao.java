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
public interface DirectorDao {
    @Query("SELECT * FROM director WHERE did = :id LIMIT 1")
    Director findDirectorById(int id);

    @Query("SELECT * FROM director WHERE full_name = :fullName LIMIT 1")
    Director findDirectorByName(String fullName);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Director director);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Director... directors);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(Director director);

    @Query("DELETE FROM director")
    void deleteAll();

    @Query("SELECT * FROM director ORDER BY full_name ASC")
    LiveData<List<Director>> getAllDirectors();
}
