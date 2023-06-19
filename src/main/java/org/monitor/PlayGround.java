package org.monitor;

import org.javapython.SpeechRecognizerV3;
import org.utils.Utils;

public class PlayGround {


    public static void main(String[] args) {
        SpeechRecognizerV3 sr = new SpeechRecognizerV3();
        String chatBotResponse = "Peter, you a great person.";
        String outputPath = "/Users/lorispodevyn/Documents/JavaBook/SpeechRecognition/tts_Loris.wav";
        sr.textToSpeech(chatBotResponse, outputPath);
        Utils.playWav(outputPath);
    }
}
