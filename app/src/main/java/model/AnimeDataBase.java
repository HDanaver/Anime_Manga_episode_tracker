package model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = AnimeEntity.class, version = 1, exportSchema = false)
public abstract class AnimeDataBase extends RoomDatabase {
    public abstract AnimeDAO animeDAO();
}