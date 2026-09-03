package com.musicplayer;

import javax.swing.ImageIcon;
import java.net.URL;
import java.awt.Image;

public class Track {
    public int id;
    public String videoId;
    public String title;
    public String artist;
    public String thumbnailUrl;
    public int duration;
    public ImageIcon icon;

    public Track(String videoId, String title, String artist, int duration, String thumbnailUrl) {
        this.videoId = videoId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Track(int id, String videoId, String title, String artist, int duration, String thumbnailUrl) {
        this(videoId, title, artist, duration, thumbnailUrl);
        this.id = id;
    }

    public void loadImage() {
        try {
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                URL url = new URL(thumbnailUrl);
                ImageIcon tempIcon = new ImageIcon(url);
                Image img = tempIcon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                this.icon = new ImageIcon(img);
            }
        } catch (Exception e) {
            this.icon = null;
        }
    }

    public String getFormattedDuration() {
        if (duration <= 0) return "0:00";
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}