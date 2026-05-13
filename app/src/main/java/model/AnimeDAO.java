package model;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface AnimeDAO {
    @Insert
    void insert(AnimeEntity anime);

    @Query("SELECT * FROM anime_table")
    List<AnimeEntity> getAllAnime();

    @Update
    void update(AnimeEntity anime);

    @Delete
    void delete(AnimeEntity anime);
}
