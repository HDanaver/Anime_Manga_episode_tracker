package com.example.anime_manga_episode_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.anime_manga_episode_tracker.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import model.AnimeDataBase;

public class MainActivity extends AppCompatActivity {
    private AnimeDataBase animeDataBase;
    private ActivityMainBinding binding;
    private SearchAdapter searchAdapter;
    private KitsuAnswerHandler kitsuAnswerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        animeDataBase = Room.databaseBuilder(getApplicationContext(),
                        AnimeDataBase.class, "anime_db")
                .fallbackToDestructiveMigration()
                .build();

        kitsuAnswerHandler = new KitsuAnswerHandler();

        searchAdapter = new SearchAdapter(new ArrayList<>(), animeDataBase.animeDAO(), kitsuData -> {
            binding.animeNameText.setText("");
            binding.animeNameText.clearFocus();
        });
        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainRecyclerView.setAdapter(searchAdapter);

        binding.animeNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() > 2) {
                    performSearch(query);
                } else {
                    searchAdapter.setData(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.savedSwapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
            startActivity(intent);
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
