package umn.ac.shovia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    static String URL_THUMBNAIL_H = "http://img.youtube.com/vi/";
    static String URL_THUMBNAIL_T = "/0.jpg";
    YouTubePlayer.OnInitializedListener youtubeInitList;
    private Context context;
    private List<String> trailerList;

    public TrailerAdapter(Context context, List<String> trailerList ) {
        this.context = context;
        this.trailerList = trailerList;
    }
    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_view,parent,false);
        TrailerAdapter.TrailerViewHolder holder = new TrailerAdapter.TrailerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerAdapter.TrailerViewHolder holder, int position) {
        final String curr = trailerList.get(position);

        Picasso.get().load(URL_THUMBNAIL_H+curr+URL_THUMBNAIL_T).fit().placeholder(R.drawable.noimage).into(holder.thumbnail);
        youtubeInitList = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                youTubePlayer.setFullscreen(true);
                youTubePlayer.loadVideo(curr);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.thumbnail.setVisibility(View.GONE);
                holder.youtube.initialize(YouTubeConfig.getApiKey(),youtubeInitList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youtube;
        ImageView thumbnail;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            youtube = itemView.findViewById(R.id.trailer);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }

    }
}
