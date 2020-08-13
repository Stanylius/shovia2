package umn.ac.shovia;

public class Detail {
    private  String castName, playAs;
    private String image;

    Detail (String castName, String playAs, String image){
        this.castName = castName;
        this.playAs = playAs;
        this.image = image;
    }

    public String getCastName() {
        return castName;
    }

    public String getPlayAs() {
        return playAs;
    }

    public String getImage() {
        return image;
    }
}
