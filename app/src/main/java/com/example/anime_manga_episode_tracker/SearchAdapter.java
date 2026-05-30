package com.example.anime_manga_episode_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime_manga_episode_tracker.databinding.SearchCardBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.AnimeDAO;
import model.AnimeEntity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<KitsuData> data;
    private AnimeDAO animeDAO;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(KitsuData kitsuData);
    }

    public SearchAdapter(List<KitsuData> data, AnimeDAO animeDAO, OnItemClickListener listener) {
        this.data = data;
        this.animeDAO = animeDAO;
        this.listener = listener;
    }

    public void setData(List<KitsuData> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.setItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final SearchCardBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchCardBinding.bind(itemView);

            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && data != null && animeDAO != null) {
                    KitsuData kitsuData = data.get(position);
                    
                    String posterUrl = null;
                    if (kitsuData.getAttributes().getPoster() != null) {
                        posterUrl = kitsuData.getAttributes().getPoster().getSmall();
                        if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getMedium();
                        if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getLarge();
                        if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getOriginal();
                    }

                    AnimeEntity entity = new AnimeEntity(
                            Integer.parseInt(kitsuData.getId()),
                            kitsuData.getAttributes().getTitle(),
                            kitsuData.getType(),
                            kitsuData.getAttributes().getTotalCount(),
                            posterUrl
                    );

                    new Thread(() -> animeDAO.insert(entity)).start();

                    if (listener != null) {
                        listener.onItemClick(kitsuData);
                    }
                }
            });
        }

        public void setItem(KitsuData kitsuData) {
            binding.textView.setText(kitsuData.getAttributes().getTitle());
            String posterUrl = null;
            if (kitsuData.getAttributes().getPoster() != null) {
                posterUrl = kitsuData.getAttributes().getPoster().getSmall();
                if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getMedium();
                if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getLarge();
                if (posterUrl == null) posterUrl = kitsuData.getAttributes().getPoster().getOriginal();
            }

            if (posterUrl != null) {
                Picasso.get().load(posterUrl).fit().into(binding.imageView);
            } else {
                Picasso.get().load(R.drawable.ic_launcher_background).fit().into(binding.imageView);
            }
        }
    }
}
