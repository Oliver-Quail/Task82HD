package com.task82hd.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.task82hd.Database.DAO.ChatDAO;
import com.task82hd.Database.DAO.MessageDAO;
import com.task82hd.Database.DAO.MiscDAO;
import com.task82hd.Database.Entity.Chat;
import com.task82hd.Database.Entity.Message;
import com.task82hd.Database.Entity.Misc;


@Database(entities = {Chat.class, Message.class, Misc.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatDAO chatDAO();
    public abstract MessageDAO messageDAO();
    public abstract MiscDAO miscDAO();
}
