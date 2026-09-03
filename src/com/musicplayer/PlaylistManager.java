package com.musicplayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/harmonystream?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "Root"; 
    
    private Connection connection;
    
    public PlaylistManager() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            throw new SQLException("Database connection failed: " + e.getMessage());
        }
    }
    
    public List<Playlist> getAllPlaylists() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                playlists.add(new Playlist(rs.getInt("playlist_id"), rs.getString("name"), 
                        rs.getString("description"), null, null));
            }
        }
        return playlists;
    }

    public void addTrackToPlaylist(int playlistId, Track track) throws SQLException {
        int trackId = getOrCreateTrack(track);
        String sql = "INSERT IGNORE INTO playlist_tracks (playlist_id, track_id, position) VALUES (?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, trackId);
            stmt.executeUpdate();
        }
    }

    public List<Track> getPlaylistTracks(int playlistId) throws SQLException {
        List<Track> tracks = new ArrayList<>();
        String sql = "SELECT t.* FROM tracks t JOIN playlist_tracks pt ON t.track_id = pt.track_id WHERE pt.playlist_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tracks.add(new Track(rs.getInt("track_id"), rs.getString("video_id"), 
                        rs.getString("title"), rs.getString("artist"), 
                        rs.getInt("duration"), rs.getString("thumbnail_url")));
            }
        }
        return tracks;
    }

    private int getOrCreateTrack(Track track) throws SQLException {
        String query = "SELECT track_id FROM tracks WHERE video_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, track.videoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("track_id");
        }

        String insert = "INSERT INTO tracks (video_id, title, artist, duration, thumbnail_url) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, track.videoId);
            stmt.setString(2, track.title);
            stmt.setString(3, track.artist);
            stmt.setInt(4, track.duration);
            stmt.setString(5, track.thumbnailUrl);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }
}