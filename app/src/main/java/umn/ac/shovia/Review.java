package umn.ac.shovia;

public class Review {
    private String name, comment;
    private String rating;

    Review (String name, String comment, String rating){
        this.name = name;
        this.comment = comment;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
    }
}
