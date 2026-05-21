package com.example.anime_manga_episode_tracker;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.anime_manga_episode_tracker.databinding.ActivityMainBinding;

import model.AnimeDataBase;


public class MainActivity extends AppCompatActivity {
    AnimeDataBase animeDataBase;
     ActivityMainBinding binding;
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
        animeDataBase = Room.databaseBuilder(this,
                        AnimeDataBase.class,
                        "anime_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void addAnime(View view) {

    }
}