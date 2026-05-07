package com.task82hd.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Chat {
    @PrimaryKey(autoGenerate = true)
    public int chatId;
    public String name;
    public String image;

}
