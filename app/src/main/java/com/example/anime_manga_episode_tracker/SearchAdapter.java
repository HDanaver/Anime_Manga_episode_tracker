package com.example.anime_manga_episode_tracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anime_manga_episode_tracker.databinding.SearchCardBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,
        parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.setItem(data.get(position));
    }

    private List<KitsuData> data;

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SearchCardBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchCardBinding.bind(itemView);
            itemView.setOnClickListener(view -> {
                notifyDataSetChanged();
            });
        }
        public KitsuData getItem(){ return data.get(getAbsoluteAdapterPosition());}
        public void setItem(KitsuData kitsuData) {
            binding.textView.setText(kitsuData.getAttributes().getTitle());
            if (kitsuData.getAttributes().getPoster().getSmall() != null){
                Picasso.get().load(kitsuData.getAttributes().getPoster().getSmall()).fit().into(binding.imageView);
            }
            else if (kitsuData.getAttributes().getPoster().getMedium() != null){
                Picasso.get().load(kitsuData.getAttributes().getPoster().getMedium()).fit().into(binding.imageView);
            }
            else if (kitsuData.getAttributes().getPoster().getLarge() != null){
                Picasso.get().load(kitsuData.getAttributes().getPoster().getLarge()).fit().into(binding.imageView);
            }
            else if (kitsuData.getAttributes().getPoster().getOriginal() != null){
                Picasso.get().load(kitsuData.getAttributes().getPoster().getOriginal()).fit().into(binding.imageView);
            }
            else{
                Picasso.get().load(R.drawable.ic_launcher_background).fit().into(binding.imageView);
            }
        }
    }
}
