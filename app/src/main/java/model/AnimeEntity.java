package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anime_table")
public class AnimeEntity {
    @PrimaryKey
    private int id;
    private String title;
    private String posterUrl;
    private String type;
    private int watchedEpisodes;
    private int totalEpisodes;
    private String status;
    private long lastUpdated;

    public AnimeEntity(int id, String title, String type, int totalEpisodes, String posterUrl){
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.type = type;
        this.totalEpisodes = totalEpisodes;
        this.watchedEpisodes = 0;
        this.lastUpdated = System.currentTimeMillis();
        this.status = "Watching";

    }

    public int getId(){return id;}
    public String getTitle() {return  title;}
    public String getPosterUrl(){return posterUrl;}
    public String getType() {return type;}
    public int getWatchedEpisodes() {return watchedEpisodes;}
    public int getTotalEpisodes() {return totalEpisodes;}
    public String getStatus() {return status;}
    public long getLastUpdated() {return lastUpdated;}

    public void setWatchedEpisodes(int wepisodes){
        this.watchedEpisodes = wepisodes;
        this.lastUpdated = System.currentTimeMillis();
    }
    public void setStatus(String status){
        this.status = status;
        this.lastUpdated = System.currentTimeMillis();
    }
    public void setLastUpdated(long update){this.lastUpdated = update;}
}
