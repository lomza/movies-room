package com.lomza.moviesroom.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
