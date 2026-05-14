package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.task82hd.Database.Entity.Chat;

@Dao
public interface ChatDAO {

    @Insert
    public abstract long createChat(Chat chat);
}
