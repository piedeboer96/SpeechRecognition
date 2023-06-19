package org.utils;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Class that takes care of building WAV files.
 */
public class Converter {

    // FORMAT
    boolean bigEndian = false;
    boolean signed = true;
    int bits = 16;
    int channels = 1;
    float sampleRate = 16000;

    // BUFFER
    byte[] byteBuffer;
    long totalSizeBuffer;

    /**
     * Bring list of float[] buffers into byte format
     * @param recordedBuffers
     * @return
     */
    public byte[] floatBuffersToBytes(List<float[]> recordedBuffers) {
        float[] buffer = concatenateBuffers(recordedBuffers);

        totalSizeBuffer = buffer.length;

        this.byteBuffer = new byte[buffer.length * 2];
        int bufferIndex = 0;

        for (int i = 0; i < this.byteBuffer.length; i++) {
            final int x = (int) (buffer[bufferIndex++] * 32767.0);
            this.byteBuffer[i] = (byte) x;
            i++;
            this.byteBuffer[i] = (byte) (x >>> 8);
        }

        return this.byteBuffer;
    }

    /**
     * Concatenate list of float buffers into one big float buffer
     * @param recordedBuffers
     * @return
     */
    public float[] concatenateBuffers(List<float[]> recordedBuffers) {
        int totalSize = 0;

        for (float[] buffer : recordedBuffers) {
            totalSize += buffer.length;
        }

        System.out.println("total size: " + totalSize);


        float[] concatenatedBuffer = new float[totalSize];
        int currentIndex = 0;

        for (float[] buffer : recordedBuffers) {
            System.arraycopy(buffer, 0, concatenatedBuffer, currentIndex, buffer.length);
            currentIndex += buffer.length;
        }

        return concatenatedBuffer;
    }

    public void makeWAV(List<float[]> recordedBuffers) throws IOException {
        // Step 0: Bring all recorded buffers into a byte[] buffer
        this.byteBuffer = floatBuffersToBytes(recordedBuffers);

        // Step 1: Determine buffer length
        int bufferLength = (int) totalSizeBuffer;
        System.out.println("Making WAV...");

        // Step 2: Build .wav file
        File out = new File("out.wav");
        AudioFormat format = new AudioFormat(44100, bits, channels, signed, bigEndian);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(this.byteBuffer);
             AudioInputStream audioInputStream = new AudioInputStream(bais, format, bufferLength)) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
        }

        convertTo16kHz("out.wav", "out16.wav");

    }


    public void convertTo16kHz(String inputFilePath, String outputFilePath) {
        try {
            // Read the input file
            AudioInputStream inputAudioStream = AudioSystem.getAudioInputStream(new File(inputFilePath));

            // Get the audio format of the input file
            AudioFormat inputAudioFormat = inputAudioStream.getFormat();

            // Create the desired output audio format
            AudioFormat outputAudioFormat = new AudioFormat(16000, inputAudioFormat.getSampleSizeInBits(),
                    1, true, false);

            // Create the audio input stream with the desired format
            AudioInputStream convertedAudioStream = AudioSystem.getAudioInputStream(outputAudioFormat, inputAudioStream);

            // Write the converted audio to the output file
            AudioSystem.write(convertedAudioStream, AudioFileFormat.Type.WAVE, new File(outputFilePath));

            // Close the streams
            inputAudioStream.close();
            convertedAudioStream.close();

            System.out.println("Conversion completed successfully.");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
