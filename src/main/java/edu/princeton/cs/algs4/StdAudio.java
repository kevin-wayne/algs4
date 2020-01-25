/******************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *  Dependencies: none
 *  
 *  Simple library for reading, writing, and manipulating .wav files.
 *
 *
 *  Limitations
 *  -----------
 *    - Assumes the audio is monaural, little endian, with sampling rate
 *      of 44,100
 *    - check when reading .wav files from a .jar file ?
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import javax.sound.sampled.Clip;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;

/**
 *  <i>Standard audio</i>. This class provides a basic capability for
 *  creating, reading, and saving audio. 
 *  <p>
 *  The audio format uses a sampling rate of 44,100 Hz, 16-bit, monaural.
 *
 *  <p>
 *  For additional documentation, see <a href="https://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public final class StdAudio {

    /**
     *  The sample rate: 44,100 Hz for CD quality audio.
     */
    public static final int SAMPLE_RATE = 44100;

    private static final int BYTES_PER_SAMPLE = 2;       // 16-bit audio
    private static final int BITS_PER_SAMPLE = 16;       // 16-bit audio
    private static final double MAX_16_BIT = 32768;
    private static final int SAMPLE_BUFFER_SIZE = 4096;

    private static final int MONO   = 1;
    private static final int STEREO = 2;
    private static final boolean LITTLE_ENDIAN = false;
    private static final boolean BIG_ENDIAN    = true;
    private static final boolean SIGNED        = true;
    private static final boolean UNSIGNED      = false;


    private static SourceDataLine line;   // to play the sound
    private static byte[] buffer;         // our internal buffer
    private static int bufferSize = 0;    // number of samples currently in internal buffer

    private StdAudio() {
        // can not instantiate
    }
   
    // static initializer
    static {
        init();
    }

    // open up an audio stream
    private static void init() {
        try {
            // 44,100 Hz, 16-bit audio, mono, signed PCM, little endian
            AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, MONO, SIGNED, LITTLE_ENDIAN);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
            
            // the internal buffer is a fraction of the actual buffer size, this choice is arbitrary
            // it gets divided because we can't expect the buffered data to line up exactly with when
            // the sound card decides to push out its samples.
            buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE/3];
        }
        catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }

        // no sound gets made before this call
        line.start();
    }

    // get an AudioInputStream object from a file
    private static AudioInputStream getAudioInputStreamFromFile(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        try {
            // first try to read file from local file system
            File file = new File(filename);
            if (file.exists()) {
                return AudioSystem.getAudioInputStream(file);
            }

            // resource relative to .class file
            InputStream is1 = StdAudio.class.getResourceAsStream(filename);
            if (is1 != null) {
                return AudioSystem.getAudioInputStream(is1);
            }

            // resource relative to classloader root
            InputStream is2 = StdAudio.class.getClassLoader().getResourceAsStream(filename);
            if (is2 != null) {
                return AudioSystem.getAudioInputStream(is2);
            }

            // give up
            else {
                throw new IllegalArgumentException("could not read '" + filename + "'");
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("could not read '" + filename + "'", e);
        }
        catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("file of unsupported audio format: '" + filename + "'", e);
        }
    }

    /**
     * Closes standard audio.
     */
    public static void close() {
        line.drain();
        line.stop();
    }
    
    /**
     * Writes one sample (between -1.0 and +1.0) to standard audio.
     * If the sample is outside the range, it will be clipped.
     *
     * @param  sample the sample to play
     * @throws IllegalArgumentException if the sample is {@code Double.NaN}
     */
    public static void play(double sample) {
        if (Double.isNaN(sample)) throw new IllegalArgumentException("sample is NaN");

        // clip if outside [-1, +1]
        if (sample < -1.0) sample = -1.0;
        if (sample > +1.0) sample = +1.0;

        // convert to bytes
        short s = (short) (MAX_16_BIT * sample);
        if (sample == 1.0) s = Short.MAX_VALUE;   // special case since 32768 not a short
        buffer[bufferSize++] = (byte) s;
        buffer[bufferSize++] = (byte) (s >> 8);   // little endian

        // send to sound card if buffer is full        
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    /**
     * Writes the array of samples (between -1.0 and +1.0) to standard audio.
     * If a sample is outside the range, it will be clipped.
     *
     * @param  samples the array of samples to play
     * @throws IllegalArgumentException if any sample is {@code Double.NaN}
     * @throws IllegalArgumentException if {@code samples} is {@code null}
     */
    public static void play(double[] samples) {
        if (samples == null) throw new IllegalArgumentException("argument to play() is null");
        for (int i = 0; i < samples.length; i++) {
            play(samples[i]);
        }
    }

    /**
     * Reads audio samples from a file (in .wav or .au format) and returns
     * them as a double array with values between -1.0 and +1.0.
     * The audio file must be 16-bit with a sampling rate of 44,100.
     * It can be mono or stereo.
     *
     * @param  filename the name of the audio file
     * @return the array of samples
     */
    public static double[] read(String filename) {

        // make sure that AudioFormat is 16-bit, 44,100 Hz, little endian
        final AudioInputStream ais = getAudioInputStreamFromFile(filename);
        AudioFormat audioFormat = ais.getFormat();

        // require sampling rate = 44,100 Hz
        if (audioFormat.getSampleRate() != SAMPLE_RATE) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only a sample rate of " + SAMPLE_RATE + " Hz\n"
                                             + "audio format: " + audioFormat);
        }

        // require 16-bit audio
        if (audioFormat.getSampleSizeInBits() != BITS_PER_SAMPLE) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only " + BITS_PER_SAMPLE + "-bit audio\n"
                                             + "audio format: " + audioFormat);
        }

        // require little endian
        if (audioFormat.isBigEndian()) {
            throw new IllegalArgumentException("StdAudio.read() currently supports only audio stored using little endian\n"
                                             + "audio format: " + audioFormat);
        }

        byte[] bytes = null;
        try {
            int bytesToRead = ais.available();
            bytes = new byte[bytesToRead];
            int bytesRead = ais.read(bytes);
            if (bytesToRead != bytesRead) {
                throw new IllegalStateException("read only " + bytesRead + " of " + bytesToRead + " bytes"); 
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not read '" + filename + "'", ioe);
        }

        int n = bytes.length;

        // little endian, mono
        if (audioFormat.getChannels() == MONO) {
            double[] data = new double[n/2];
            for (int i = 0; i < n/2; i++) {
                // little endian, mono
                data[i] = ((short) (((bytes[2*i+1] & 0xFF) << 8) | (bytes[2*i] & 0xFF))) / ((double) MAX_16_BIT);
            }
            return data;
        }

        // little endian, stereo
        else if (audioFormat.getChannels() == STEREO) {
            double[] data = new double[n/4];
            for (int i = 0; i < n/4; i++) {
                double left  = ((short) (((bytes[4*i+1] & 0xFF) << 8) | (bytes[4*i + 0] & 0xFF))) / ((double) MAX_16_BIT);
                double right = ((short) (((bytes[4*i+3] & 0xFF) << 8) | (bytes[4*i + 2] & 0xFF))) / ((double) MAX_16_BIT);
                data[i] = (left + right) / 2.0;
            }
            return data;
        }

        // TODO: handle big endian (or other formats)
        else throw new IllegalStateException("audio format is neither mono or stereo");
    }

    /**
     * Saves the double array as an audio file (using .wav or .au format).
     *
     * @param  filename the name of the audio file
     * @param  samples the array of samples
     * @throws IllegalArgumentException if unable to save {@code filename}
     * @throws IllegalArgumentException if {@code samples} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} extension is not {@code .wav}
     *         or {@code .au}
     */
    public static void save(String filename, double[] samples) {
        if (filename == null) {
            throw new IllegalArgumentException("filenameis null");
        }
        if (samples == null) {
            throw new IllegalArgumentException("samples[] is null");
        }

        // assumes 16-bit samples with sample rate = 44,100 Hz
        // use 16-bit audio, mono, signed PCM, little Endian
        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, MONO, SIGNED, LITTLE_ENDIAN);
        byte[] data = new byte[2 * samples.length];
        for (int i = 0; i < samples.length; i++) {
            int temp = (short) (samples[i] * MAX_16_BIT);
            if (samples[i] == 1.0) temp = Short.MAX_VALUE;   // special case since 32768 not a short
            data[2*i + 0] = (byte) temp;
            data[2*i + 1] = (byte) (temp >> 8);   // little endian
        }

        // now save the file
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, samples.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else {
                throw new IllegalArgumentException("file type for saving must be .wav or .au");
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("unable to save file '" + filename + "'", ioe);
        }
    }



    /**
     * Plays an audio file (in .wav, .mid, or .au format) in a background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if unable to play {@code filename}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public static synchronized void play(final String filename) {
        new Thread(new Runnable() {
            public void run() {
                AudioInputStream ais = getAudioInputStreamFromFile(filename);
                stream(ais);
            }
        }).start();
    }


    // https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
    // play a wav or aif file
    // javax.sound.sampled.Clip fails for long clips (on some systems), perhaps because
    // JVM closes (see remedy in loop)
    private static void stream(AudioInputStream ais) {
        SourceDataLine line = null;
        int BUFFER_SIZE = 4096; // 4K buffer

        try {
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            byte[] samples = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                line.write(samples, 0, count);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        finally {
            if (line != null) {
                line.drain();
                line.close();
            }
        }
    }

    /**
     * Loops an audio file (in .wav, .mid, or .au format) in a background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public static synchronized void loop(String filename) {
        if (filename == null) throw new IllegalArgumentException();

        final AudioInputStream ais = getAudioInputStreamFromFile(filename);

        try {
            Clip clip = AudioSystem.getClip();
            // Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // keep JVM open
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                       Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


   /***************************************************************************
    * Unit tests {@code StdAudio}.
    ***************************************************************************/

    // create a note (sine wave) of the given frequency (Hz), for the given
    // duration (seconds) scaled to the given volume (amplitude)
    private static double[] note(double hz, double duration, double amplitude) {
        int n = (int) (StdAudio.SAMPLE_RATE * duration);
        double[] a = new double[n+1];
        for (int i = 0; i <= n; i++)
            a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
        return a;
    }

    /**
     * Test client - play an A major scale to standard audio.
     *
     * @param args the command-line arguments
     */
    /**
     * Test client - play an A major scale to standard audio.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        
        // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i <= StdAudio.SAMPLE_RATE; i++) {
            StdAudio.play(0.5 * Math.sin(2*Math.PI * freq * i / StdAudio.SAMPLE_RATE));
        }
        
        // scale increments
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz = 440.0 * Math.pow(2, steps[i] / 12.0);
            StdAudio.play(note(hz, 1.0, 0.5));
        }


        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        StdAudio.close(); 
    }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
