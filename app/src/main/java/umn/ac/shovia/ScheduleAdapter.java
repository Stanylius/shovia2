package umn.ac.shovia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private Context context;
    private List<Schedule> scheduleList;


    public ScheduleAdapter(Context context, List<Schedule>scheduleList ) {
        this.context = context;
        this.scheduleList = scheduleList;
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list,parent,false);
        ScheduleViewHolder holder = new ScheduleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.cinema.setText(schedule.getCinema());
        List<String> isiSchedule = schedule.getSchedule();
        for(int i=0;i<isiSchedule.size();i++){
            holder.jadwal.append(isiSchedule.get(i)+" ");
        }
    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView cinema,jadwal;
        LinearLayout linearLayout;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            cinema = itemView.findViewById(R.id.cinema);
            jadwal = itemView.findViewById(R.id.jadwal);

        }
    }
}
