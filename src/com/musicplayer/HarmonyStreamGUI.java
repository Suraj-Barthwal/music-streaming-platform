package com.musicplayer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HarmonyStreamGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);
    
    // Search UI
    private JTextField searchField = new JTextField(30);
    private DefaultListModel<Track> searchModel = new DefaultListModel<>();
    private JList<Track> searchList = new JList<>(searchModel);

    // Playlist UI
    private DefaultListModel<Playlist> listModel = new DefaultListModel<>();
    private JList<Playlist> playlistJList = new JList<>(listModel);
    private DefaultListModel<Track> pTrackModel = new DefaultListModel<>();
    private JList<Track> pTrackList = new JList<>(pTrackModel);

    private Player player = new Player();
    private PlaylistManager db;
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private JLabel statusLabel = new JLabel(" Ready");

    public HarmonyStreamGUI() {
        super("HarmonyStream v4.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);

        try { db = new PlaylistManager(); } catch (Exception e) { e.printStackTrace(); }

        initSearchPanel();
        initPlaylistPanel();

        add(container, BorderLayout.CENTER);
        add(createControlBar(), BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);
    }

    private void initSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton sBtn = new JButton("Search");
        JButton goP = new JButton("My Playlists");
        top.add(searchField); top.add(sBtn); top.add(goP);

        searchList.setCellRenderer(new TrackRenderer());
        searchList.setFixedCellHeight(100);

        JPanel bot = new JPanel();
        JButton play = new JButton("Play");
        JButton addB = new JButton("Add to Playlist");
        bot.add(play); bot.add(addB);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(searchList), BorderLayout.CENTER);
        panel.add(bot, BorderLayout.SOUTH);

        sBtn.addActionListener(e -> search());
        goP.addActionListener(e -> { loadPlaylists(); cardLayout.show(container, "P"); });
        play.addActionListener(e -> play(searchList.getSelectedValue()));
        addB.addActionListener(e -> addToDb(searchList.getSelectedValue()));

        container.add(panel, "S");
    }

    private void initPlaylistPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        
        JButton back = new JButton("← Back");
        JButton playBtn = new JButton("Play Selected");

        playlistJList.setPreferredSize(new Dimension(200, 0));
        pTrackList.setCellRenderer(new TrackRenderer());
        pTrackList.setFixedCellHeight(100);

        topPanel.add(back, BorderLayout.WEST);
        topPanel.add(playBtn, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(playlistJList), BorderLayout.WEST);
        panel.add(new JScrollPane(pTrackList), BorderLayout.CENTER);

        back.addActionListener(e -> cardLayout.show(container, "S"));
        playBtn.addActionListener(e -> playFromPlaylist(pTrackList.getSelectedValue()));
        playlistJList.addListSelectionListener(e -> loadTracks(playlistJList.getSelectedValue()));
        
        container.add(panel, "P");
    }

    private JPanel createControlBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton stop = new JButton("Stop");
        bar.add(stop);
        stop.addActionListener(e -> player.stop());
        return bar;
    }

    private void search() {
        executor.submit(() -> {
            try {
                String json = APIHandler.searchAPI(searchField.getText());
                List<Track> results = Parser.parseSearchResults(json);
                for (Track t : results) t.loadImage();
                SwingUtilities.invokeLater(() -> {
                    searchModel.clear();
                    for (Track t : results) searchModel.addElement(t);
                });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private void play(Track t) {
        if (t == null) return;
        executor.submit(() -> {
            try {
                String url = APIHandler.getStreamAPI(t.videoId);
                player.play(url);
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private void playFromPlaylist(Track t) {
        if (t == null) return;
        executor.submit(() -> {
            try {
                // Search for the track using its title and artist
                String query = t.title + " " + t.artist;
                String json = APIHandler.searchAPI(query);
                List<Track> results = Parser.parseSearchResults(json);
                
                // Play the first result found
                if (!results.isEmpty()) {
                    String url = APIHandler.getStreamAPI(results.get(0).videoId);
                    player.play(url);
                }
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    private void addToDb(Track t) {
        try { db.addTrackToPlaylist(1, t); } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadPlaylists() {
        try {
            List<Playlist> ps = db.getAllPlaylists();
            listModel.clear();
            for (Playlist p : ps) listModel.addElement(p);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTracks(Playlist p) {
        if (p == null) return;
        executor.submit(() -> {
            try {
                List<Track> ts = db.getPlaylistTracks(p.id);
                for (Track t : ts) t.loadImage();
                SwingUtilities.invokeLater(() -> {
                    pTrackModel.clear();
                    for (Track t : ts) pTrackModel.addElement(t);
                });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HarmonyStreamGUI().setVisible(true));
    }
}