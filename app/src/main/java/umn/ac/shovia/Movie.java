package umn.ac.shovia;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Movie implements Parcelable {
    private String id;
    private String poster;
    private String releaseDate;
    private String title;
    private String desc;
    private String country;
    private String background;
    private List<String> trailer;


    private String duration;

    public Movie(String id, String poster, String releaseDate, String title, String desc, String country, String duration, String background, List<String> trailer){
        this.id = id;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.title = title;
        this.desc = desc;
        this.country = country;
        this.duration = duration;
        this.background = background;
        this.trailer = trailer;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        poster = in.readString();
        releaseDate = in.readString();
        title = in.readString();
        desc = in.readString();
        country = in.readString();
        background = in.readString();
        trailer = in.createStringArrayList();
        duration = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(poster);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(country);
        dest.writeString(background);
        dest.writeStringList(trailer);
        dest.writeString(duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getCountry() {
        return country;
    }

    public String getDuration() {
        return duration;
    }

    public String getId(){
        return id;
    }

    public String getPoster (){
        return poster;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public String getTitle(){
        return title;
    }
    public String getDesc(){
        return desc;
    }

    public String getBackground() {
        return background;
    }

    public List<String> getTrailer() {
        return trailer;
    }
}
