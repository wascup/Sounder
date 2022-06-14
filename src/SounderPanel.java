import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

import static javax.swing.BorderFactory.createEmptyBorder;

public class SounderPanel extends JPanel {
    SounderFrame parentFrame;
    Color backgroundColor = Color.decode("#494949");
    Color foregroundColor = Color.decode("#606060");
    Color selectedColor = Color.decode("#2a2a2a");
    boolean isPlaying = false;
    AdvancedPlayer player;
    ArrayList<Song> Songs = new ArrayList<>();
    DefaultListModel SongModel = new DefaultListModel();
    ImageIcon songAlbum;
    JLabel currentSongAlbum;
    int albumSize = 200;
    int songFrame = 0;
    boolean shuffle = false;
    JList<Song> songList = new JList<>();
    SongInfo currentSongTitle = new SongInfo("Title");
    SongInfo currentSongArtist = new SongInfo("Artist");
    SongInfo currentSongAlbumTitle = new SongInfo("Album");
    SongInfo currentSongDuration = new SongInfo("Duration");
    SongInfo currentSongTrack = new SongInfo("Track");
    SongInfo currentSongYear = new SongInfo("Year");
    SounderButton playButton = new SounderButton("▶");
    SounderButton shuffleButton = new SounderButton("");
    SounderButton nextSongButton = new SounderButton("");
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
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
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
        ImageIcon shuffleIcon = new ImageIcon("icons/shuffle.png");
        Image shuffleImage = shuffleIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        shuffleButton.setIcon(new ImageIcon(shuffleImage));

        nextSongButton.setBounds(albumSize + 75, 345, 40, 40);
        ImageIcon forwardIcon = new ImageIcon("icons/forward.png");
        Image forwardImage = forwardIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        nextSongButton.setIcon(new ImageIcon(forwardImage));
        add(nextSongButton);

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
        repaint();
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
    void playSong(boolean newsong) {
        if(isPlaying) {
            stopSong();
        }
        if (songSelected()) {
            try {
                File songFile = new File(String.valueOf(Songs.get(songList.getSelectedIndex()).getFile()));
                //create new thread to handle constant mp3 streaming
                Thread songThread = new Thread(() -> {
                    try {
                        player = new AdvancedPlayer(new FileInputStream(songFile));
                        player.setPlayBackListener(new PlaybackListener() {
                            @Override
                            public void playbackFinished(PlaybackEvent event) {
                                songFrame = event.getFrame();
                                if (shuffle && !newsong) {
                                    var rand = new Random();
                                    int randomIndex = rand.nextInt(songList.getModel().getSize());
                                    songList.setSelectedIndex(randomIndex);
                                    playSong(true);
                                }
                            }
                        });
                        if (newsong) {
                            player.play();
                        } else {
                            player.play(songFrame, Integer.MAX_VALUE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                songThread.start();
                isPlaying = true;
                parentFrame.setTitle(Songs.get(songList.getSelectedIndex()).getTitle() + " - Sounder");
                updateAlbumArt(getSelectedSong());
                updateSongInfo(getSelectedSong());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
        void stopSong() {
        try {
            player.stop();
            isPlaying = false;
            songFrame = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //for play button toggling
    void toggleSong() {
        if (isPlaying) {
            stopSong();
        } else {
            playSong(false);
        }
        System.out.println(isPlaying);
    }
    //updates the album art of the current song
    void updateAlbumArt(Song song) {
        var albumArt = song.getAlbumart();
        songAlbum = albumArt;
        Image img = albumArt.getImage().getScaledInstance(albumSize, albumSize, Image.SCALE_SMOOTH);
        currentSongAlbum.setIcon(new ImageIcon(img));
    }
    //updates all visual info about the song for UI
    void updateSongInfo(Song song) {
        currentSongTitle.setText(song.getTitle());
        currentSongArtist.setText(song.getArtist());
        currentSongAlbumTitle.setText(song.getAlbum());
        currentSongDuration.setText(song.getLength() + "");
        currentSongTrack.setText("Track #: " + song.getTrack());
        currentSongYear.setText(song.getYear());
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
                    playSong(true);
                }
            }
        });
        nextSongButton.addActionListener(e -> {
            if (songSelected()) {
                if (songList.getSelectedIndex() == Songs.size() - 1) {
                    songList.setSelectedIndex(0);
                } else {
                    songList.setSelectedIndex(songList.getSelectedIndex() + 1);
                }
                playSong(true);
            }
        });
        shuffleButton.addActionListener(e -> {
            if (shuffle) {
                shuffle = false;
                shuffleButton.setBackground(foregroundColor);
            } else {
                shuffle = true;
                shuffleButton.setBackground(selectedColor);
            }
        });
        volumeSlider.addChangeListener(e -> setVolume(volumeSlider.getValue() / 100.0f));
    }

    //checks if a song is selected
    boolean songSelected() {
        return songList.getSelectedIndex() != -1;
    }
    Song getSelectedSong() {
        return Songs.get(songList.getSelectedIndex());
    }
    public void setVolume(float ctrl) {
        try {
            Mixer.Info[] infos = AudioSystem.getMixerInfo();
            for (Mixer.Info info : infos) {
                Mixer mixer = AudioSystem.getMixer(info);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();
                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                        volume.setValue(ctrl);
                    }
                    port.close();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro\n" + e);
        }
    }
}