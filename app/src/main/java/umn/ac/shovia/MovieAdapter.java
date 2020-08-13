package umn.ac.shovia;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>implements Filterable {
    private Context context;
    private List<Movie> movieList;
    private String userName;
    private List<Movie> movieListHolder;
    private static String URL = "https://firebasestorage.googleapis.com/v0/b/shovia-85fd5.appspot.com/o";


    public MovieAdapter(Context context, List<Movie>movieList, String userName ) {
        this.context = context;
        this.movieList = movieList;
        this.userName = userName;
        movieListHolder = new ArrayList<>(movieList);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_layout_items, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.title.setText(movie.getTitle());
        holder.date.setText(movie.getReleaseDate());
        holder.desc.setText(movie.getDesc());
        holder.country.setText(movie.getCountry());
        holder.duration.setText(movie.getDuration());
        Picasso.get().load(URL+movie.getPoster()).fit().placeholder(R.drawable.noimage).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView title, date, desc, country, duration;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.textViewTitle);
            date = itemView.findViewById(R.id.textViewDate);
            desc = itemView.findViewById(R.id.textViewShortDesc);
            country = itemView.findViewById(R.id.textViewCountry);
            duration = itemView.findViewById(R.id.textViewDuration);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(context,DetailMovie.class);
            intent.putExtra("Movie",movieList.get(pos)).putExtra("userName",userName);
            context.startActivity(intent);
        }
    }
    @Override
    public Filter getFilter() {
        return filterLagu;
    }

    private Filter filterLagu = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Movie> filteredList = new ArrayList<>();
            if (constraint==null || constraint.length()==0){
                filteredList.addAll(movieListHolder);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for( Movie object: movieListHolder){
                    if(object.getTitle().toLowerCase().contains(filterPattern)){
                        filteredList.add(object);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movieList.clear();
            movieList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
