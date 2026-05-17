package com.task82hd.Database.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Misc {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int mode;
    public boolean hasAgreedToOnline;
}
