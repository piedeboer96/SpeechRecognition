package org.monitor;

import org.javapython.SpeechRecognizerV3;

public class PlayGround {


    public static void main(String[] args) {
        SpeechRecognizerV3 sr = new SpeechRecognizerV3();
        String chatBotResponse = "Peter, you a great person.";
        sr.textToSpeech(chatBotResponse,"/Users/lorispodevyn/Documents/JavaBook/SpeechRecognition/tts_Loris.wav");
    }
}
