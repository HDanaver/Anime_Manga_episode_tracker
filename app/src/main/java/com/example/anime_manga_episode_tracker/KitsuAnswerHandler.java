package com.example.anime_manga_episode_tracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KitsuAnswerHandler {

    KitsuHandler apiService;

    public KitsuAnswerHandler() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kitsu.io/api/edge/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.apiService = retrofit.create(KitsuHandler.class);
    }

    public void searchAnime(String query, KitsuSearchCallback callback) {
        apiService.searchAnime(query, 10).enqueue(new Callback<KitsuResponse>() {
            @Override
            public void onResponse(Call<KitsuResponse> call, Response<KitsuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Sikertelen keresési válasz a szervertől.");
                }
            }

            @Override
            public void onFailure(Call<KitsuResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // 2. Részletek lekérése metódus
    public void getAnimeDetails(String id, KitsuDetailsCallback callback) {
        apiService.getAnimeDetails(id).enqueue(new Callback<KitsuResponse>() {
            @Override
            public void onResponse(Call<KitsuResponse> call, Response<KitsuResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    // Mivel ID alapján kértük, a lista legelső (0.) eleme a mi animénk
                    KitsuAttributes details = response.body().getData().get(0).getAttributes();
                    callback.onSuccess(details);
                } else {
                    callback.onError("Sikertelen részlet-válasz a szervertől.");
                }
            }

            @Override
            public void onFailure(Call<KitsuResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // --- INTERFÉSZEK A MAINACTIVITY-VEL VALÓ KOMMUNIKÁCIÓHOZ ---
    public interface KitsuSearchCallback {
        void onSuccess(List<KitsuData> results);
        void onError(String errorMessage);
    }

    public interface KitsuDetailsCallback {
        void onSuccess(KitsuAttributes details);
        void onError(String errorMessage);
    }
}

