package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

import com.task82hd.Database.Entity.Chat;

import java.util.List;

@Dao
public interface ChatDAO {

    @Insert
    public abstract long createChat(Chat chat);

    @Query("SELECT * FROM chat")
    public abstract List<Chat> getChats();

    @Query("SELECT * FROM chat WHERE chatId = :chatId LIMIT 1")
    public abstract Chat getChat(int chatId);

    @Update
    public abstract void updateChat(Chat chat);
}
