import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import jaco.mp3.player.MP3Player;

import static javax.swing.BorderFactory.createEmptyBorder;

public class SounderPanel extends JPanel {
    SounderFrame parentFrame;
    Color backgroundColor = Color.decode("#494949");
    Color foregroundColor = Color.decode("#606060");
    boolean isPlaying = false;
    MP3Player player;
    ArrayList<Song> Songs = new ArrayList<>();
    DefaultListModel SongModel = new DefaultListModel();
    ImageIcon songAlbum;
    JLabel currentSongAlbum;
    public int albumSize = 200;
    JList<Song> songList = new JList<>();
    SongInfo currentSongTitle = new SongInfo("Title");
    SongInfo currentSongArtist = new SongInfo("Artist");
    SongInfo currentSongAlbumTitle = new SongInfo("Album");
    SongInfo currentSongDuration = new SongInfo("Duration");
    SongInfo currentSongTrack = new SongInfo("Track");
    SongInfo currentSongYear = new SongInfo("Year");
    SounderButton playButton = new SounderButton("▶");
    SounderButton shuffleButton = new SounderButton("\uD83D\uDD01");
    SounderButton repeatButton = new SounderButton("\uD83D\uDD00");
    SounderButton addNewSong = new SounderButton("+");
    SounderButton removeSong = new SounderButton("-");
    JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    JSlider progressSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);

    public SounderPanel(SounderFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(null);
        Thread guiThread = new Thread(this::setupGUI);
        guiThread.start();
        addEventListeners();
    }
    void setupGUI() {
        songAlbum = new ImageIcon();
        setBackground(backgroundColor);
        songAlbum = new ImageIcon("placeholders/album.png");
        currentSongAlbum = new JLabel(songAlbum);
        currentSongAlbum.setBounds(10, 10, albumSize, albumSize);
        add(currentSongAlbum);
        //Song List fit the rest of the UI
        songList.setBackground(foregroundColor);
        songList.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(songList);
        //removes annoying border around the list
        scrollPane.setBorder(createEmptyBorder());
        scrollPane.setBounds(albumSize + 20, 10, 255, 300);
        add(scrollPane);
        addNewSong.setBounds(albumSize + 225, 310, 25, 25);
        add(addNewSong);
        removeSong.setBounds(albumSize + 250, 310, 25, 25);
        add(removeSong);
        //all controls for player
        playButton.setBounds(albumSize + 30, 345, 40, 40);
        add(playButton);
        shuffleButton.setBounds(albumSize - 15, 345, 40, 40);
        add(shuffleButton);
        repeatButton.setBounds(albumSize + 75, 345, 40, 40);
        add(repeatButton);

        //controls for volume control
        volumeSlider.setBounds(albumSize + 130, 350, 100, 40);
        volumeSlider.setBackground(backgroundColor);
        volumeSlider.setForeground(foregroundColor);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        add(volumeSlider);

        //Makeshift song length slider
        progressSlider.setBounds(50, 400, 400, 40);
        progressSlider.setBackground(backgroundColor);
        progressSlider.setForeground(foregroundColor);
        add(progressSlider);

        //Song info labels
        currentSongTitle.setBounds(10, albumSize + 20, 200, 25);
        add(currentSongTitle);
        currentSongArtist.setBounds(10, albumSize + 45, 200, 25);
        add(currentSongArtist);
        currentSongAlbumTitle.setBounds(10, albumSize + 70, 200, 25);
        add(currentSongAlbumTitle);
        currentSongDuration.setBounds(10, albumSize + 95, 200, 25);
        add(currentSongDuration);
        currentSongTrack.setBounds(10, albumSize + 120, 200, 25);
        add(currentSongTrack);
        currentSongYear.setBounds(10, albumSize + 145, 200, 25);
        add(currentSongYear);

        //makes the song list look better by padding it
        songList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(createEmptyBorder(5, 5, 2, 5));
                return label;
            }
        });

        revalidate();
    }
    //adds the songs to the arraylist and UI
    void addSongToList(Song newSong) {
        Songs.add(newSong);
        SongModel.add(SongModel.getSize(), newSong.getTitle());
        songList.setModel(SongModel);
        songList.add(Songs.get(Songs.size() - 1).getComponent());
    }
    //adds songs based on what the user chooses
    Song[] addSong() {
        Song newSong = new Song();
        Song[] songs = newSong.makeSong();
        return songs;
    }
    //gets the selected song and removes it from the list and UI
    void removeSong() {
        if (songSelected()) {
            Songs.remove(songList.getSelectedIndex());
            SongModel.remove(songList.getSelectedIndex());
            songList.setModel(SongModel);
        }
    }
    //plays the selected song and calls methods to update the UI
    void playSong() {
        if(isPlaying) {
            player.stop();
            isPlaying = false;
        }
        if (songSelected()) {
            File songFile = new File(String.valueOf(Songs.get(songList.getSelectedIndex()).getFile()));
            player = new MP3Player(songFile);
            player.play();
            isPlaying = true;
            parentFrame.setTitle(Songs.get(songList.getSelectedIndex()).getTitle() + " - Sounder");
            updateAlbumArt(songList.getSelectedIndex());
            updateSongInfo(songList.getSelectedIndex());
        }
    }
    //updates the album art of the current song
    void updateAlbumArt(int index) {
        var albumArt = Songs.get(index).getAlbumart();
        songAlbum = albumArt;
        Image img = albumArt.getImage().getScaledInstance(albumSize, albumSize, Image.SCALE_SMOOTH);
        currentSongAlbum.setIcon(new ImageIcon(img));
    }
    //updates all visual info about the song for UI
    void updateSongInfo(int index) {
        currentSongTitle.setText(Songs.get(index).getTitle());
        currentSongArtist.setText(Songs.get(index).getArtist());
        currentSongAlbumTitle.setText(Songs.get(index).getAlbum());
        currentSongDuration.setText(Songs.get(index).getLength() + "");
        currentSongTrack.setText("Track #: " + Songs.get(index).getTrack());
        currentSongYear.setText(Songs.get(index).getYear());
    }
    //adds event listeners to all buttons
    void addEventListeners() {
        addNewSong.addActionListener(e -> {
            Song[] songs = addSong();
            for (Song song : songs) {
                addSongToList(song);
            }
        });
        removeSong.addActionListener(e -> removeSong());
        playButton.addActionListener(e -> toggleSong());
        songList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON2) {
                    removeSong();
                    return;
                }
                if (e.getClickCount() == 2) {
                    playSong();
                }

            }
        });
    }
    //for play button toggling
    void toggleSong() {
        if(isPlaying) {
            player.stop();
            isPlaying = false;
            playButton.setText("▶");
        } else {
            if(player == null) {
                playSong();
            }
            player.play();
            isPlaying = true;
            playButton.setText("||");
        }
    }
    //checks if a song is selected
    boolean songSelected() {
        return songList.getSelectedIndex() != -1;
    }
}