package com.lomza.moviesroom.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author Antonina
 */
@Entity(tableName = "director",
        indices = {@Index("full_name")})
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
