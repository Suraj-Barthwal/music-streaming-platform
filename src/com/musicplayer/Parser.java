package com.musicplayer;

import java.util.*;

import org.json.JSONObject;

public class Parser {
    public static List<Track> parseSearchResults(String jsonOutput) 
    {
        List<Track> tracks = new ArrayList<>();
        String[] lines = jsonOutput.split("\n");//splits the string into many strings by \n
        
        for (String line : lines) 
        {
            if (line.trim().isEmpty()) 
            	continue;//if empty do nothing and move to next
            
            try 
            {
                JSONObject item = new JSONObject(line);//puts line into json object
                //adds to tracks vector or list as they are in key value pair
                tracks.add(new Track(
                    item.getString("id"),
                    item.optString("title", "Unknown Title"),//for front end wwhen creqted...
                    item.optString("uploader", "Unknown Artist"),
                    item.optInt("duration", 0),
                    item.optString("thumbnail", "")
                ));
            } 
            catch (Exception e) 
            {
            	continue; 
            }
        }
        return tracks;
    }

    
    
    
    
    public static String parseStreamUrl(String jsonString) 
    {
        try
        {
        	 return jsonString.trim();
        } 
        catch (Exception e) 
        {
            return "";
        }
    }
}