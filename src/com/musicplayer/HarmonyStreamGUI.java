package com.musicplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HarmonyStreamGUI extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JList<Track> resultList;
    private DefaultListModel<Track> listModel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JTextArea logArea;

    private Player player;
    private ExecutorService executor;

    public HarmonyStreamGUI() {
        super("HarmonyStream");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        player = new Player();
        executor = Executors.newSingleThreadExecutor();

        initComponents();
        setupLayout();
        setupActions();
    }

    private void initComponents() {
        searchField = new JTextField(30);
        searchButton = new JButton("Search");
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");
        progressBar = new JProgressBar();
        statusLabel = new JLabel("Ready");
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
    }

    private void setupLayout() {
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JScrollPane scrollPane = new JScrollPane(resultList);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(0, 100));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(logScrollPane, BorderLayout.EAST);
    }

    private void setupActions() {
        searchButton.addActionListener(e -> searchMusic());

        playButton.addActionListener(e -> playSelected());
        pauseButton.addActionListener(e -> updateStatus("Pause is not supported. Use terminal to pause (press space)."));
        stopButton.addActionListener(e -> player.stop());
    }

    private void searchMusic() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            updateStatus("Please enter a search query");
            return;
        }

        updateStatus("Searching...");
        executor.submit(() -> {
            try {
                String jsonResult = APIHandler.searchAPI(query);
                List<Track> results = Parser.parseSearchResults(jsonResult);
                SwingUtilities.invokeLater(() -> {
                    listModel.clear();
                    for (Track track : results) {
                        listModel.addElement(track);
                    }
                    updateStatus("Found " + results.size() + " results");
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    updateStatus("Search failed: " + e.getMessage());
                    logMessage("Search error: " + e.getMessage());
                });
            }
        });
    }

    private void playSelected() {
        int index = resultList.getSelectedIndex();
        if (index == -1) {
            updateStatus("Please select a song to play");
            return;
        }

        Track selected = listModel.getElementAt(index);
        String videoId = selected.videoId;

        if (videoId == null) {
            updateStatus("Invalid selection");
            return;
        }

        updateStatus("Loading and playing...");
        executor.submit(() -> {
            try {
                String streamUrl = APIHandler.getStreamAPI(videoId);
                player.play(streamUrl);
                SwingUtilities.invokeLater(() -> updateStatus("Playing..."));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    updateStatus("Playback failed: " + e.getMessage());
                    logMessage("Playback error: " + e.getMessage());
                });
            }
        });
    }

    private void updateStatus(String status) {
        statusLabel.setText(status);
        logMessage(status);
    }

    private void updateProgress(int progress) {
        progressBar.setValue(progress);
    }

    private void logMessage(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HarmonyStreamGUI().setVisible(true);
        });
    }
}
