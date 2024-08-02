package labs.lab9;

public class Song {

    private String title;
    private String artist;
    private boolean friends;
    private boolean family;
    private boolean coworkers;
    private String category;
    private double runningtime;
    private String notes;

    public Song(String title, String artist, boolean friends, boolean family,
                boolean coworkers, String category, double runningtime, String notes) {
        
        this.title = title;
        this.artist = artist;
        this.friends = friends;
        this.family = family;
        this.coworkers = coworkers;
        this.category = category;
        this.runningtime = runningtime;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean getFriends() {
        return friends;
    }

    public boolean getFamily() {
        return family;
    }

    public boolean getCoworkers() {
        return coworkers;
    }

    public String getCategory() {
        return category;
    }

    public double getRunningTime() {
        return runningtime;
    }

    public String getNotes() {
        return notes;
    }
}
