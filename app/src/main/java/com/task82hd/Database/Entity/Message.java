package com.task82hd.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int messageId;
    public String contents;
    public boolean isAi;
    public int chatId;
}
