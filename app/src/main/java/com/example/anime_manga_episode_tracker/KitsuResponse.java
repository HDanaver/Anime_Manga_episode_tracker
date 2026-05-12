package com.example.anime_manga_episode_tracker;

import java.util.List;

public class KitsuResponse {
    private List<KitsuData> data;

    public List<KitsuData> getData() {
        return data;
    }

}

class KitsuData{
    private String id;
    private String type;
    private KitsuAttributes attributes;

    public String getId() { return id;}
    public String getType() { return type;}
    public KitsuAttributes getAttributes() { return attributes;}
}

class KitsuAttributes{
    private String title;
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

class PosterImage{
    private String medium;
    private String large;

    public String getMedium() {return medium;}
    public String getLarge() {return large;}


}
