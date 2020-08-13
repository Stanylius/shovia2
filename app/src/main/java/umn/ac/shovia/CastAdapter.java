package umn.ac.shovia;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder>  {
    private Context context;
    private List<Detail> castList;

    private static String URL = "https://firebasestorage.googleapis.com/v0/b/shovia-85fd5.appspot.com/o";
    public CastAdapter(Context context, List<Detail>castList ) {
        this.context = context;
        this.castList = castList;
    }
    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item,parent,false);
        CastViewHolder holder = new CastViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Detail detail = castList.get(position);

        holder.CastName.setText(detail.getCastName());
        holder.PlayAs.setText(detail.getPlayAs());
        Picasso.get().load(URL+detail.getImage()).fit().placeholder(R.drawable.noimage).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder{
       CircleImageView image;
       TextView CastName, PlayAs;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.castImage);
            CastName = itemView.findViewById(R.id.castName);
            PlayAs = itemView.findViewById(R.id.playAs);
        }
   }


}
