package view;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Manages playing different sounds in the game.
 */
public class SoundPanel {

    public static Clip backgroundMusic;
    public static Clip correctAnswerSound;
    public static Clip lockSound;

    /**
     * Plays the background music from the specified file path.
     *
     * @param filePath The file path of the background music.
     */
    public static void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            backgroundMusic = (Clip) AudioSystem.getLine(info); // Store the Clip instance
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            //gainControl.setValue(-75.0f);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound for a correct answer.
     */
    public static void playCorrectAnswerSound() {
        playSound("CorrectAnswer.wav");
    }

    /**
     * Plays the sound for locking/unlocking doors.
     */
    public static void playLockSound() {
        playSound("LockSound.wav");
    }

    /**
     * Plays the sound for winning the game.
     */
    public static void playWinSound() {
        playSound("Winner.wav");
    }

    /**
     * Plays the sound for losing the game.
     */
    public static void playLoseSound() {
        playSound("Loser.wav");
    }

    private static void playSound(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip soundClip = (Clip) AudioSystem.getLine(info); // Store the Clip instance
            soundClip.open(audioStream);
            soundClip.start(); // Play the sound once
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the background music if it's currently playing.
     */
    public static void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
}