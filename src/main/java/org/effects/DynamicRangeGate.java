package org.effects;

import org.effects.SoundPressureLevel;

/**
 * Dynamic Range Gate with variable release time and
 * with threshold calculated based on dBSPL
 */
public class DynamicRangeGate {

    public boolean gate_open = false;
    private final int releaseTimeMs;                    // release time
    private final float threshold;                      // threshold
    final float[] zeroBuffer = new float[1024];         // block size 1024
    SoundPressureLevel spl = new SoundPressureLevel();  // spl
    private long gateOpenTime;                          // time gate opened

    /**
     * Dynamic Range Gate
     * @param threshold based on dbspl
     * @param releaseTimeMs releasetime in ms
     * @param sampleRate samplerate
     */
    public DynamicRangeGate(float threshold, int releaseTimeMs, int sampleRate) {
        this.threshold = threshold;
        this.releaseTimeMs = releaseTimeMs;
        this.gate_open = true;
    }

    /**
     * Gate the float audio buffer
     * @param buffer audio_buffer
     * @return
     */
    public float[] process(float[] buffer) {
        double level = spl.soundPressureLevel(buffer);
        //System.out.println(level);
        //System.out.println("level: " + level);

        if (level > threshold) {
            // Gate is open
            gateOpenTime = System.currentTimeMillis();
            //System.out.println("open");
            return buffer;
        } else if (System.currentTimeMillis() - gateOpenTime < releaseTimeMs) {
            // Gate is still within the release time
            //System.out.println("release");
            return buffer;
        } else {
            //System.out.println("close");
            return zeroBuffer; // Return the zero buffer when release time is over
        }
    }

}
