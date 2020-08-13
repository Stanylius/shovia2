package umn.ac.shovia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotifListAdapter extends RecyclerView.Adapter<NotifListAdapter.NotifViewHolder>  {
    private final LayoutInflater mInflater;
    private List<RNotif> daftarRNotif;
    NotifListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.notif_items,
                parent,false);
        return new NotifViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        if(daftarRNotif != null){
            RNotif rnotif = daftarRNotif.get(position);
            holder.judulFilem.setText(rnotif.getTitle());
            holder.textNotif.setText(rnotif.getText());
        }

    }

    @Override
    public int getItemCount() {
        if(daftarRNotif != null){
            return daftarRNotif.size();
        } else {
            return 0;
        }

    }
    public RNotif getMahasiswaAtPosition(int posisi){
        return daftarRNotif.get(posisi);
    }
    void setRNotif(List<RNotif> rnotifs){
        daftarRNotif = rnotifs;
        notifyDataSetChanged();
    }

    class NotifViewHolder extends RecyclerView.ViewHolder{
        private TextView judulFilem,textNotif;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            judulFilem = itemView.findViewById(R.id.judulFilem);
            textNotif = itemView.findViewById(R.id.textNotif);
        }
    }
}

