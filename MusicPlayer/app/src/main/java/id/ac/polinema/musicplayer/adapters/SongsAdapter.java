package id.ac.polinema.musicplayer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import id.ac.polinema.musicplayer.R;
import id.ac.polinema.musicplayer.models.Track;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ItemViewHolder> {

    private List<Track> dataList = new ArrayList<>();


    public void setDataList(List<Track> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.song_item, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(dataList.get(position));


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView txtArtistName, txtSong;

        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.song_poster);
            txtArtistName = itemView.findViewById(R.id.song_artist);
            txtSong = itemView.findViewById(R.id.song_name);

        }

        public void bind(Track track) {
            txtSong.setText(track.getName());
            txtArtistName.setText(track.getArtist().getName());
            //loadImage(ivThumbnail,track.getUrl());

        }


        public static void loadImage(ImageView view, String imageUrl) {
            Glide.with(view.getContext())
                    .load(imageUrl).apply(new RequestOptions().circleCrop())
                    .into(view);
        }

    }
}
