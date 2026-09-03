package com.musicplayer;

import java.util.*;
import org.json.JSONObject;

public class Parser {
    public static List<Track> parseSearchResults(String jsonOutput) {
        List<Track> tracks = new ArrayList<>();
        String[] lines = jsonOutput.split("\n");
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            try {
                JSONObject item = new JSONObject(line);
                tracks.add(new Track(
                    item.getString("id"),
                    item.optString("title", "Unknown Title"),
                    item.optString("uploader", "Unknown Artist"),
                    item.optInt("duration", 0),
                    item.optString("thumbnail", "")
                ));
            } catch (Exception e) {
                continue; 
            }
        }
        return tracks;
    }

    public static String parseStreamUrl(String jsonString) {
        return jsonString.trim();
    }
}