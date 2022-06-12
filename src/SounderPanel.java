import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class SounderPanel extends JPanel {
    ArrayList<Song> songs = new ArrayList<Song>();
    ImageIcon songAlbum;
    public int albumSize = 200;

    public SounderPanel() {
        setupGUI();
    }
    void setupGUI() {
        setLayout(null);
        songAlbum = new ImageIcon();
        setBackground(Color.decode("#494949"));
        songAlbum = new ImageIcon("placeholders/album.png");
        JLabel currentSongAlbum = new JLabel(songAlbum);
        currentSongAlbum.setBounds(10, 10, albumSize, albumSize);
        add(currentSongAlbum);
    }


    Song addSong() {
        Song newSong = new Song();
        newSong.makeSong();
        return newSong;
    }

}
