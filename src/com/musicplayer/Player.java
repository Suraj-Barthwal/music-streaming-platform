package com.musicplayer;

import java.io.IOException;

public class Player {
    private Process playerProcess;

    public void play(String streamUrl) {
        stop();
        try {
            String[] command = {
                "ffplay", "-nodisp", "-autoexit", 
                "-reconnect", "1", "-reconnect_streamed", "1", streamUrl.trim()
            };
            playerProcess = new ProcessBuilder(command).start();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void stop() {
        if (playerProcess != null && playerProcess.isAlive()) {
            playerProcess.destroy();
        }
    }
}