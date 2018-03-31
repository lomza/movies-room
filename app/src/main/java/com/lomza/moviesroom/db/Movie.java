package com.lomza.moviesroom.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author Antonina
 */
@Entity(tableName = "movie",
        foreignKeys = @ForeignKey(entity = Director.class,
                parentColumns = "did",
                childColumns = "directorId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("title"), @Index("directorId")})
public class Movie {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mid")
    public int id;

    @ColumnInfo(name = "title")
    @NonNull
    public String title;

    @ColumnInfo(name = "directorId")
    public int directorId;

    public Movie(@NonNull String title, int directorId) {
        this.title = title;
        this.directorId = directorId;
    }
}
