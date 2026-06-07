package com.example.anime_manga_episode_tracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.anime_manga_episode_tracker.databinding.LibraryActivityBinding;

import model.AnimeDataBase;
import model.AnimeEntity;

public class LibraryActivity extends AppCompatActivity {

    private LibraryActivityBinding binding;
    private ViewAdapter viewAdapter;
    private AnimeDataBase animeDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LibraryActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        animeDataBase = Room.databaseBuilder(getApplicationContext(),
                        AnimeDataBase.class, "anime_db")
                .fallbackToDestructiveMigration()
                .build();

        viewAdapter = new ViewAdapter(anime -> {
            if (anime.getWatchedEpisodes() < anime.getTotalEpisodes() || anime.getTotalEpisodes() == 0) {
                int newEpisodes = anime.getWatchedEpisodes() + 1;
                anime.setWatchedEpisodes(newEpisodes);
                if (anime.getTotalEpisodes() > 0 && newEpisodes >= anime.getTotalEpisodes()) {
                    anime.setStatus("Completed");
                }
                new Thread(() -> animeDataBase.animeDAO().update(anime)).start();
            }
        });

        binding.libraryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.libraryRecyclerView.setAdapter(viewAdapter);

        animeDataBase.animeDAO().getAllAnime().observe(this, animeEntities -> {
            viewAdapter.setData(animeEntities);
        });

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
                Toast.makeText(LibraryActivity.this, "Törölve: " + anime.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(binding.libraryRecyclerView);

        binding.backButton.setOnClickListener(v -> finish());
    }
}
