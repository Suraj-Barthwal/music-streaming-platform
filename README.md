# HarmonyStream

HarmonyStream is a Java-based desktop music streaming application designed to provide a simple and convenient way to search, play, and manage music.

The project is built using Java and provides a graphical user interface for interacting with music, playlists, and playback controls.

## Features

- Search for songs and retrieve music information
- Stream and play music
- Play, pause, and control music playback
- Create and manage playlists
- Add and remove tracks from playlists
- Display track information
- Custom track rendering in the user interface
- Simple and user-friendly graphical interface
- API-based music search and data retrieval

## Technologies Used

- **Java**
- **Java Swing** for the graphical user interface
- **Java HTTP APIs** for retrieving music information
- **JSON Parsing** for processing API responses
- **Java Collections** for playlist and track management
- **Java Modules** for project organization

## Screenshots
<img width="688" height="452" alt="image-1" src="https://github.com/user-attachments/assets/bfa67fb0-99ef-400e-b0f6-3db6392ea102" />

<img width="960" height="1020" alt="image-2" src="https://github.com/user-attachments/assets/5c5d643f-50a7-4904-aee7-8484bc881532" />
## Project Structure

```text
music_streaming/
│
├── src/
│   ├── module-info.java
│   │
│   └── com/
│       └── musicplayer/
│           ├── APIHandler.java
│           ├── HarmonyStreamGUI.java
│           ├── Main.java
│           ├── Parser.java
│           ├── Player.java
│           ├── Playlist.java
│           ├── PlaylistManager.java
│           ├── Track.java
│           └── TrackRenderer.java
│
└── README.md

