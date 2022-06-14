import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.mpatric.mp3agic.*;

public class Song {
    private File file;
    private Image albumart;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String track;
    private String filePath;
    private double length;
    public Song[] makeSong() {
        ArrayList<Song> songs = new ArrayList<>();
        FileDialog chooser = new FileDialog(new JFrame(), "Select a song", FileDialog.LOAD);
        chooser.setMultipleMode(true);
        chooser.setVisible(true);

        for (File file : chooser.getFiles()) {
            Song newSong = new Song();
            newSong.file = file;
            newSong.filePath = file.getAbsolutePath();
            String filename = file.getName();
            newSong.title = filename;
            newSong.artist = "Unknown";
            newSong.album = "Unknown";
            newSong.year = "Unknown";
            newSong.genre = "Unknown";
            newSong.track = "Unknown";
            newSong.length = 0;
            newSong.albumart = new ImageIcon("placeholders/noart.png").getImage();

            try {
                Mp3File mp3file = new Mp3File(file);

                if (mp3file.hasId3v2Tag()) {
                    ID3v1 tag = mp3file.getId3v2Tag();
                    newSong.file = file;
                    newSong.filePath = file.getAbsolutePath();
                    newSong.title = tag.getTitle();
                    newSong.artist = tag.getArtist();
                    newSong.album = tag.getAlbum();
                    newSong.year = tag.getYear();
                    newSong.genre = String.valueOf(tag.getGenre());
                    newSong.track = tag.getTrack();
                    byte[] AlbumArt = mp3file.getId3v2Tag().getAlbumImage();
                    if(AlbumArt != null) {
                        newSong.albumart = new ImageIcon(AlbumArt).getImage();
                    }
                    newSong.filePath = file.getAbsolutePath();
                    newSong.length = mp3file.getLengthInMilliseconds();
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }



            songs.add(newSong);
        }

        return songs.toArray(new Song[songs.size()]);
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getAlbum() {
        return album;
    }
    public ImageIcon getAlbumart() {
        return new ImageIcon(albumart);
    }
    public String getYear() {
        return year;
    }
    public String getTrack() {
        return track;
    }
    public File getFile() {
        return file;
    }
    public double getLength() {
        return length;
    }
    public Component getComponent() {
        return new JLabel(getTitle());
    }
}
