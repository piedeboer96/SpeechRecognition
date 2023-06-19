package org.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Utils {

    // Method to play a .wav file
    public static void playWav(String wavFilePath) {
        try {
            // Get an AudioInputStream from the specified file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(wavFilePath));
            // Get a Clip that can be used for playing back the AudioInputStream
            Clip audioClip = AudioSystem.getClip();
            // Open the AudioInputStream as a Clip
            audioClip.open(audioStream);
            // Create a synchronization object
            Object syncObject = new Object();
            // Add a listener to the audioClip. It notifies the syncObject when the clip finishes playing
            audioClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    synchronized (syncObject) {
                        syncObject.notifyAll();
                    }
                }
            });
            // Start the audioClip
            audioClip.start();
            // Wait until the audioClip has finished playing
            synchronized (syncObject) {
                syncObject.wait();
            }
        }
        // Handle possible exceptions
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
