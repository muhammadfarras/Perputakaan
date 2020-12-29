package org.example;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class SoundNotif {
    protected String location;
    protected Media media;
    public SoundNotif (String sound){
        ClassLoader classLoader = getClass().getClassLoader();
        this.location = sound;
        Media media = new Media(classLoader.getResource(sound).toString());
        this.media = media;
    }
    public void turnOn (){
        MediaPlayer mediaPlayer = new MediaPlayer(this.media);
        mediaPlayer.play();
    }
}
