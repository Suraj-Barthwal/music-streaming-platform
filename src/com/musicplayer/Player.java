package com.musicplayer;

import java.io.IOException;

public class Player {
    private Process playerProcess;

    
    
    public void play(String streamUrl) 
    {
        stop();//safety
        try {
            String[] command = {
            		 "ffplay",
            		    "-reconnect", "1",
            		    "-reconnect_streamed", "1",
            		    "-reconnect_delay_max", "5",
            		    streamUrl
                };
            
            ProcessBuilder pb = new ProcessBuilder(command);
            
            
            
            pb.inheritIO(); //to share the terminal of both ffplay and java
            
            playerProcess = pb.start();
            System.out.println(">>> FFplay started. Check the new window for playback.");
            
        	}
        catch (IOException e) 
        {
            System.err.println("Player error: " + e.getMessage());
        }
    }

    
    
    public void stop() 
    {
        if (playerProcess != null && playerProcess.isAlive()) 
        {
            playerProcess.destroy();
        }
    }
}