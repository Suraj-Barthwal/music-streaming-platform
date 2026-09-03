package com.musicplayer;

import java.io.*;
import java.util.stream.Collectors;

public class APIHandler {
    
    public static String searchAPI(String query) throws Exception {
        // Fetches top 10 results with metadata in JSON format
        return runYTDLP("ytsearch10:" + query, "--dump-json", "--flat-playlist", "--quiet");
    }
    
    public static String getStreamAPI(String videoId) throws Exception {
        // Gets the direct audio streaming URL
        return runYTDLP("https://www.youtube.com/watch?v=" + videoId,
                "-f", "bestaudio",
                "-g",
                "--quiet",
                "--no-warnings");
    }
    
    private static String runYTDLP(String... args) throws Exception {
        String[] command = new String[args.length + 1];
        command[0] = "yt-dlp"; 
        System.arraycopy(args, 0, command, 1, args.length);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String result = reader.lines().collect(Collectors.joining("\n"));
            process.waitFor();
            return result;
        }
    }
}