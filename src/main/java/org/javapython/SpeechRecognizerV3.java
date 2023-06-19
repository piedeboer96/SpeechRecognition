package org.javapython;

import java.io.*;

public class SpeechRecognizerV3 {

    private BufferedWriter writer;
    private BufferedReader reader;
    private Process process;

    public SpeechRecognizerV3() {
        try {
            // Get the absolute path of the Python script
            String pythonScriptPath = "/Users/lorispodevyn/Documents/JavaBook/SpeechRecognition/python/SR3.py";
            String pythonExecutablePath = "/opt/homebrew/opt/python@3.10/libexec/bin/python3";

            // Create the Python script command with the absolute path
            String[] cmd = {
                    pythonExecutablePath,
                    pythonScriptPath
            };

            // Create a new process builder
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);

            // Redirect error stream to avoid deadlocks and consume the output stream from the subprocess
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();

            // Create writer to send input to the script
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            // Create reader to read output from the script
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String transcribe(String audioFilePath) {
        String transcribedText = "";

        try {
            // Send the audio file path to the Python script
            writer.write(audioFilePath);
            writer.newLine();
            writer.flush();

            // Read the output of the script
            String line;
            while (!(line = reader.readLine()).startsWith("Transcription:")) {
                // wait until we receive the transcription result
            }

            // Get the transcription result
            transcribedText = line.substring("Transcription:".length()).trim();

            System.out.println("Transcription: " + transcribedText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transcribedText;
    }

    public void textToSpeech(String text, String outputPath) {
        try {
            //TODO Change the path
            String pythonScriptPath = "/Users/lorispodevyn/Documents/JavaBook/SpeechRecognition/python/TTS.py";
            String pythonExecutablePath = "/opt/homebrew/opt/python@3.10/libexec/bin/python3";

            // Command to run your Python script with the text and output file as arguments
            String[] cmd = {
                    pythonExecutablePath,
                    pythonScriptPath,
                    text,
                    outputPath
            };

            // Run the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();  // Wait for the script to finish

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            // Send EOF to indicate we're done
            writer.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
