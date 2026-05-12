package com.example.anime_manga_episode_tracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface KitsuHandler {
    @GET("anime")
    Call<KitsuResponse> searchAnime(
      @Query("filter[text]") String query,
      @Query("page[limit]") int limit
    );

    @GET("manga")
    Call<KitsuResponse> searchManga(
            @Query("filter[text]") String query,
            @Query("page[limit]") int limit
    );
}
