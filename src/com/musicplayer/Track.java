package com.musicplayer;

public class Track {
    public String videoId;
    public String title;
    public String artist;
    public String thumbnailUrl;
    public int duration;
    
    public Track(String videoId, String title, String artist, int duration, String thumbnailUrl)
    {
        this.videoId = videoId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFormattedDuration() 
    {
        if (duration <= 0) 
        	return "0:00";
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() 
    {
        return String.format("%s - %s [%s]", artist, title, getFormattedDuration());
    }
}