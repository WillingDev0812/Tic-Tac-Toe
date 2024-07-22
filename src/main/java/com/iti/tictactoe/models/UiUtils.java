package com.iti.tictactoe.models;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

public class UiUtils {

    private static MediaPlayer mediaPlayer;

    public static void addHoverAnimation(Button button) {
        button.setOnMouseEntered(e -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(button.translateXProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(button.translateXProperty(), 10)),
                    new KeyFrame(Duration.seconds(0.4), new KeyValue(button.translateXProperty(), 0))
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(true);
            timeline.play();
            button.setUserData(timeline); // Store timeline in button's user data
        });

        button.setOnMouseExited(e -> {
            Timeline timeline = (Timeline) button.getUserData();
            if (timeline != null) {
                timeline.stop();
                button.setTranslateX(0); // Reset position
            }
        });
    }

    public static void playBackgroundMusic() {
        String musicFile = "/com/iti/tictactoe/Sounds/bella-ciao-rise-of-legend-dance-house-version-background-vlog-music.mp3";
        try {
            Media media = new Media(Objects.requireNonNull(UiUtils.class.getResource(musicFile)).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repeat the music
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playSoundEffect() {
        String soundFilePath = "/com/iti/tictactoe/Sounds/buttonSoundEffect.wav";
        try {
            Media sound = new Media(UiUtils.class.getResource(soundFilePath).toExternalForm());
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            soundPlayer.setOnEndOfMedia(soundPlayer::dispose); // Dispose the player after the sound is played
            soundPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
