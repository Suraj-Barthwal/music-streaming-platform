module music_streaming {
    requires javafx.controls;
    requires javafx.graphics;
    requires org.json;
	requires java.desktop; // Required for your Parser class
    requires java.sql;
    // This line allows JavaFX to see and "start" your GUI class
    opens com.musicplayer to javafx.graphics;
    
    exports com.musicplayer;
}