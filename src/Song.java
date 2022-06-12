import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.*;

public class Song {
    private File file;
    private byte[] albumart;
    private String title;
    private String artist;
    private String album;
    private String year;
    private String genre;
    private String track;
    private String comment;
    private String lyrics;
    private String filePath;
    private double length;
//    public Song() {
//        this.file = Song;
//        this.filePath = Song.getAbsolutePath();
//        this.title = "";
//        this.artist = "";
//        this.album = "";
//        this.year = "";
//        this.genre = "";
//        this.track = "";
//        this.comment = "";
//        this.lyrics = "";
//        this.length = 0;
//    }
    public Song makeSong() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                this.file = chooser.getSelectedFile();
                this.filePath = file.getAbsolutePath();
                Mp3File mp3file = new Mp3File(filePath);
                ID3v2 id3v2Tag = null;
                if (mp3file.hasId3v2Tag()) {
                    id3v2Tag = mp3file.getId3v2Tag();
                    this.title = id3v2Tag.getTitle();
                    this.artist = id3v2Tag.getArtist();
                    this.album = id3v2Tag.getAlbum();
                    this.year = id3v2Tag.getYear();
                    this.genre = id3v2Tag.getGenreDescription();
                    this.track = id3v2Tag.getTrack();
                    this.comment = id3v2Tag.getComment();
                    this.lyrics = id3v2Tag.getLyrics();
                }
                this.length = mp3file.getLengthInSeconds();
                this.albumart = id3v2Tag.getAlbumImage();
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }
        return this;
    }
    public Component getComponent() {
        return new JLabel(this.getTitle());
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumart(byte[] albumart) {
        this.albumart = albumart;
    }
    public ImageIcon getAlbumart() {
        return new ImageIcon(albumart);
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getFile() {
        return file;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
