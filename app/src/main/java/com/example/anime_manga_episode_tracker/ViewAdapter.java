package com.example.anime_manga_episode_tracker;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime_manga_episode_tracker.databinding.ItemCardBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.AnimeEntity;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    
    private List<AnimeEntity> data = new ArrayList<>();
    private final OnAnimeActionListener listener;

    public interface OnAnimeActionListener {
        void onAddEpisode(AnimeEntity anime);
    }

    public ViewAdapter(OnAnimeActionListener listener) {
        this.listener = listener;
    }

    public void setData(List<AnimeEntity> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.ViewHolder holder, int position) {
        holder.setItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public List<AnimeEntity> getData() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardBinding binding;
        private final Handler handler = new Handler(Looper.getMainLooper());
        private Runnable autoIncrementRunnable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCardBinding.bind(itemView);
        }

        public void setItem(AnimeEntity animeEntity) {
            binding.animeNameTextView.setText(animeEntity.getTitle());
            
            String totalEpisodesText = animeEntity.getTotalEpisodes() > 0 
                    ? String.valueOf(animeEntity.getTotalEpisodes()) 
                    : "?";
            binding.episodesTextVIew.setText(animeEntity.getWatchedEpisodes() + "/" + totalEpisodesText);
            
            binding.statusTextView.setText(animeEntity.getStatus());

            int total = animeEntity.getTotalEpisodes();
            int watched = animeEntity.getWatchedEpisodes();
            if (total > 0) {
                int progress = (watched * 100) / total;
                binding.progressionProgressBar.setProgress(progress);
                binding.percentageTextView.setText(progress + "%");
            } else {
                binding.progressionProgressBar.setProgress(0);
                binding.percentageTextView.setText("N/A");
            }

            if (animeEntity.getPosterUrl() != null && !animeEntity.getPosterUrl().isEmpty()) {
                Picasso.get().load(animeEntity.getPosterUrl()).into(binding.posterImageView);
            } else {
                Picasso.get().load(R.drawable.ic_launcher_background).into(binding.posterImageView);
            }

            // Folyamatos növelés logikája
            binding.addEpisodeButton.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (autoIncrementRunnable != null) {
                            handler.removeCallbacks(autoIncrementRunnable);
                        }
                        // Első növelés azonnal
                        if (listener != null) {
                            listener.onAddEpisode(animeEntity);
                        }
                        // Ismétlődő futtatás elindítása egy rövid késleltetés után
                        autoIncrementRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null) {
                                    listener.onAddEpisode(animeEntity);
                                }
                                handler.postDelayed(this, 150);
                            }
                        };
                        handler.postDelayed(autoIncrementRunnable, 500);
                        v.setPressed(true);
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (autoIncrementRunnable != null) {
                            handler.removeCallbacks(autoIncrementRunnable);
                            autoIncrementRunnable = null;
                        }
                        v.setPressed(false);
                        v.performClick();
                        return true;
                }
                return false;
            });
        }
    }
}
