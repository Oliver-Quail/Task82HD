package com.task82hd.Database.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.task82hd.Database.Entity.Message;

@Dao
public interface MessageDAO {

    @Insert
    public abstract void createMessage(Message message);
}
