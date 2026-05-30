package com.example.anime_manga_episode_tracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.anime_manga_episode_tracker.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import model.AnimeDataBase;
import model.AnimeEntity;

public class MainActivity extends AppCompatActivity {
    private AnimeDataBase animeDataBase;
    private ActivityMainBinding binding;
    private SearchAdapter searchAdapter;
    private ViewAdapter viewAdapter;
    private KitsuAnswerHandler kitsuAnswerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Sötét mód kapcsoló kezelése
        binding.darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Adatbázis inicializálása
        animeDataBase = Room.databaseBuilder(getApplicationContext(),
                        AnimeDataBase.class, "anime_db")
                .fallbackToDestructiveMigration()
                .build();

        // Kitsu API handler inicializálása
        kitsuAnswerHandler = new KitsuAnswerHandler();

        // --- Keresési RecyclerView ---
        searchAdapter = new SearchAdapter(new ArrayList<>(), animeDataBase.animeDAO(), kitsuData -> {
            binding.searchResultContainer.setVisibility(View.GONE);
            binding.animeNameText.setText("");
            binding.animeNameText.clearFocus();
        });
        binding.resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.resultRecyclerView.setAdapter(searchAdapter);

        // --- Fő (Követett animék) RecyclerView ---
        viewAdapter = new ViewAdapter(anime -> {
            if (anime.getWatchedEpisodes() < anime.getTotalEpisodes() || anime.getTotalEpisodes() == 0) {
                int newEpisodes = anime.getWatchedEpisodes() + 1;
                anime.setWatchedEpisodes(newEpisodes);
                
                // Ha elérte a maximumot, váltson Completed-re
                if (anime.getTotalEpisodes() > 0 && newEpisodes >= anime.getTotalEpisodes()) {
                    anime.setStatus("Completed");
                }

                new Thread(() -> animeDataBase.animeDAO().update(anime)).start();
            }
        });
        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRecyclerView.setAdapter(viewAdapter);

        // Adatok figyelése az adatbázisból
        animeDataBase.animeDAO().getAllAnime().observe(this, animeEntities -> {
            viewAdapter.setData(animeEntities);
        });

        // Swipe-to-delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                AnimeEntity anime = viewAdapter.getData().get(position);
                new Thread(() -> animeDataBase.animeDAO().delete(anime)).start();
            }
        }).attachToRecyclerView(binding.mainRecyclerView);

        // Keresés figyelése az EditText-ben
        binding.animeNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() > 2) {
                    performSearch(query);
                    binding.searchResultContainer.setVisibility(View.VISIBLE);
                } else {
                    binding.searchResultContainer.setVisibility(View.GONE);
                    searchAdapter.setData(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        kitsuAnswerHandler.searchAnime(query, new KitsuAnswerHandler.KitsuSearchCallback() {
            @Override
            public void onSuccess(List<KitsuData> results) {
                runOnUiThread(() -> searchAdapter.setData(results));
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("KitsuSearch", "Error: " + errorMessage);
            }
        });
    }
}
