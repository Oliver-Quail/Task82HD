package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.task82hd.Database.Entity.Message;

import java.util.List;

@Dao
public interface MessageDAO {

    @Insert
    public abstract void createMessage(Message message);

    @Query("SELECT * FROM message where chatId = :chatId")
    public abstract List<Message> getMessagesByChat(int chatId);
}
