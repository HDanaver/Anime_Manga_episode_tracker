package model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AnimeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AnimeEntity anime);

    @Query("SELECT * FROM anime_table ORDER BY title ASC")
    LiveData<List<AnimeEntity>> getAllAnime();

    @Update
    void update(AnimeEntity anime);

    @Delete
    void delete(AnimeEntity anime);
}
