package umn.ac.shovia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_items,parent,false);
        ReviewAdapter.ReviewViewHolder holder = new ReviewAdapter.ReviewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.name.setText(review.getName());
        holder.comment.setText(review.getComment());
        holder.rating.setRating(Float.valueOf(review.getRating()));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class  ReviewViewHolder extends RecyclerView.ViewHolder{
        RatingBar rating;
        TextView name, comment;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.review);
        }
    }
}
