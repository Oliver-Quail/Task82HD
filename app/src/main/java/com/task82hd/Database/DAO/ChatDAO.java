package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

import com.task82hd.Database.Entity.Chat;

import java.util.List;

@Dao
public interface ChatDAO {

    @Insert
    public abstract long createChat(Chat chat);

    @Query("SELECT * FROM chat")
    public abstract List<Chat> getChats();
}
