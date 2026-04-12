package com.musicplayer;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Player player = new Player();
    private static List<Track> lastResults = new ArrayList<>();
    
    public static void main(String[] args) {
    	
    	
        System.out.println("HarmonyStream");
        runLoop();//main function that is getting used
        
       
    }
    
    
    
    private static void runLoop() 
    {
        while (true) 
        {
            System.out.print("\nSearch song ('stop' / 'exit'): ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit")) 
            	break;
            if (input.equalsIgnoreCase("stop")) 
            {
                player.stop();
                continue;
            }
            if (input.isEmpty()) 
            	continue;

            handleSearchAndSelect(input);//sends to the next function
        }
    }

    
    
    private static void handleSearchAndSelect(String query) 
    {
        try 
        {
            System.out.println("Searching...");
            
            //1st
            String json = APIHandler.searchAPI(query);
            
            //2nd
            lastResults = Parser.parseSearchResults(json);//3 results in vector or lsit whatever is it in java
            //searched result with their links
            
            if (lastResults.isEmpty()) {
                System.out.println("No results found.");//  :3
                return;
            }

            for (int i = 0; i < lastResults.size(); i++) 
            {
                Track t = lastResults.get(i);//print top 3 results
                System.out.printf("[%d] %s - %s (%s)\n", i + 1, t.artist, t.title, t.getFormattedDuration());//to print three results
            }
            
            System.out.print("\nSelect number to play (Enter to cancel): ");
            String choiceStr = scanner.nextLine().trim();//.trim to remove spaces from the ends.
            if (choiceStr.isEmpty()) 
            	return;

            int choice = Integer.parseInt(choiceStr) - 1;//because array 0 to n-1
            if (choice >= 0 && choice < lastResults.size()) 
            {
                playSelected(lastResults.get(choice));//next function
            }
        } 
        catch (Exception e) 
        {
            System.err.println("Process Error: " + e.getMessage());
        }
    }

    
    
    
    private static void playSelected(Track track) throws Exception 
    {
    	
        System.out.println("Extracting audio for: " + track.title);
        
        //to get the streaming link from ytdlp
        String streamJson = APIHandler.getStreamAPI(track.videoId);
        
        
        System.out.println("DEBUG: " + streamJson);// used for debugging......... will be removed when completed like in last phase

        String url = Parser.parseStreamUrl(streamJson);// to get only the URL
        
        if (url.isEmpty()) //if nothing is there
        {
        	
            System.out.println("Failed to stream.");
        } 
        else 
        {
        	//to start playing ;3
            player.play(url);
        }
    }
}