package com.example.anime_manga_episode_tracker;

import com.google.gson.annotations.SerializedName;

public class KitsuAttributes{
    @SerializedName("canonicalTitle")
    private String title;
    @SerializedName("posterImage")
    private PosterImage poster;
    private Integer episodeCount;
    private Integer chapterCount;

    public String getTitle() {return title;}
    public PosterImage getPoster() {return poster;}
    public Integer getEpisodeCount() {return episodeCount;}
    public Integer getChapterCount() {return chapterCount;}

    public int getTotalCount(){
        if (episodeCount != null){ return episodeCount;}
        if (chapterCount != null){ return chapterCount;}
        return 0;
    }
}
