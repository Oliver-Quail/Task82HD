package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.task82hd.Database.Entity.Misc;

@Dao
public interface MiscDAO {

    @Insert
    public abstract void createMisc(Misc misc);

    @Query("SELECT * FROM misc LIMIT 1")
    public abstract Misc getMisc();

    @Query("UPDATE misc set mode = :mode")
    public abstract void setMode(int mode);
}
