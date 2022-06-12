import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;



public class SounderPanel extends JPanel {
    ArrayList<Song> Songs = new ArrayList<Song>();
    DefaultListModel SongModel = new DefaultListModel();
    ImageIcon songAlbum;
    public int albumSize = 200;
    JList<Song> songList = new JList<Song>();

    public SounderPanel() {
        setLayout(null);
        Thread guiThread = new Thread(this::setupGUI);
        guiThread.start();
    }
    void setupGUI() {
        songAlbum = new ImageIcon();
        setBackground(Color.decode("#494949"));
        songAlbum = new ImageIcon("placeholders/album.png");
        JLabel currentSongAlbum = new JLabel(songAlbum);
        currentSongAlbum.setBounds(10, 10, albumSize, albumSize);
        add(currentSongAlbum);
        songList.setBounds(albumSize + 20, 10, 275, 300);
        songList.setBackground(Color.decode("#606060"));
        songList.setForeground(Color.WHITE);
        Thread addSong = new Thread(() -> {
           Song newSong = addSong();
           addSongToList(newSong);
        });
        addSong.start();
        add(songList);
        revalidate();
        repaint();
    }
    void addSongToList(Song newSong) {
        Songs.add(newSong);
        SongModel.add(SongModel.getSize(), newSong.getTitle());
        songList.setModel(SongModel);
        songList.add(Songs.get(Songs.size() - 1).getComponent());
    }

    Song addSong() {
        Song newSong = new Song();
        newSong.makeSong();
        return newSong;
    }

}
