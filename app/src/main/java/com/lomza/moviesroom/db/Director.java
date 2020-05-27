package com.lomza.moviesroom.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * @author Antonina
 */
@Entity(tableName = "director",
        indices = {@Index(value = "full_name", unique = true)})
public class Director {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "did")
    public int id;

    @ColumnInfo(name = "full_name")
    @NonNull
    public String fullName;

    @Ignore
    public int age;

    public Director(@NonNull String fullName) {
        this.fullName = fullName;
    }
}
