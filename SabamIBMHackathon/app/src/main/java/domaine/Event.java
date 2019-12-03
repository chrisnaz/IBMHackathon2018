package domaine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Regex;

public class Event {

    private String name;
    private List<String> playlist;

    private String date;
    private long latitude;
    private long longitude;
    private String location;
    private String artist;

    public Event() {
        date = "";
        playlist = new ArrayList<>();
        artist = "";
    }

    public void createPlaylist(String result) {
        String dateR = Regex.findDate(result);
        List<String> returnedPlaylist = Regex.findSongName(result);
        String artistName = Regex.findArtist(result);
        String locationR = Regex.findLocation(result);

        if (!dateR.equals("")) {
            date = dateR;
        }

        if (!artistName.equals("")) {
            artist = artistName;
        }
        if (!locationR.equals(""))
            location = locationR;


        if (returnedPlaylist.size() != 0) {
            playlist = returnedPlaylist;
        }

    }

    public String getArtist() {
        return artist;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public List<String> getPlaylist() {
        return playlist;
    }
}
