package umn.ac.shovia;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Schedule {
    private List<String> schedule;
    private String cinema;

    private String duration;

    public Schedule(List<String> schedule, String cinema){
        this.schedule = schedule;
        this.cinema = cinema;
    }

    public List<String> getSchedule(){
        return schedule;
    }
    public String getCinema(){
        return cinema;
    }

}
