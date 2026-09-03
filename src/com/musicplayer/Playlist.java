package com.musicplayer;

import java.sql.Timestamp;

public class Playlist {
    public int id;
    public String name;
    public String description;
    
    public Playlist(int id, String name, String description, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    @Override
    public String toString() {
        return name; // This is what shows up in the JList sidebar
    }
}