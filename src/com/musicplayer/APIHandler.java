package com.musicplayer;

import java.io.*;
import java.util.stream.Collectors;

public class APIHandler {
    
    public static String searchAPI(String query) throws Exception 
    {
        //3 for top 3 results 
    	//--dump-json to write all the meta data in the terminal as json 
    	//--flatplaylist only calls simple data not too deep
    	//dlp has its own warnings it silences or not prints those.
        return runYTDLP("ytsearch3:" + query, "--dump-json", "--flat-playlist", "--quiet");
    }
    
    
    
    
    public static String getStreamAPI(String videoId) throws Exception 
    {
        //general starter with the song specific vdeoid... other thing same  as before with teh best audio thing.
        return runYTDLP("https://www.youtube.com/watch?v=" + videoId,
        	    "-f", "bestaudio",
        	    "-g",
        	    "--quiet",
        	    "--no-warnings");
    }
    
    
    //variable arguements
    private static String runYTDLP(String... args) throws Exception 
    {
        String[] command = new String[args.length + 1];//array created
        command[0] = "yt-dlp"; 
        
        System.arraycopy(args, 0, command, 1, args.length);// copies the arguements to teh command array

        ProcessBuilder pb = new ProcessBuilder(command);//built process
        
        pb.redirectErrorStream(true);//to pass everything errors and all outputs together to the same stream
      //start process
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) //the programs out becomes input stream for Java
        {
            String result = reader.lines().collect(Collectors.joining("\n"));//joins all the lines with \n in between
            process.waitFor();// to wait for the process to complete
            
            
            return result;
        }
    }
}