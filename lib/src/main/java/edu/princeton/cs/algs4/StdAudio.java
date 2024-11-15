/******************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *  Dependencies: none
 *
 *  Simple library for reading, writing, and manipulating audio.
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import javax.sound.sampled.Clip;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import java.util.LinkedList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *  The {@code StdAudio} class provides static methods for
 *  playing, reading, and saving audio.
 *  It uses a simple audio model that allows you
 *  to send one sample to the sound card at a time.
 *  Each sample is a real number between –1.0 and +1.0.
 *  The samples are played in real time using a sampling
 *  rate of 44,100 Hz.
 *  In addition to playing individual samples, standard audio supports
 *  reading, writing, and playing audio files in a variety of standard formats.
 *  <p>
 *  <b>Getting started.</b>
 *  To use this class, you must have {@code StdAudio} in your Java classpath.
 *  Here are three possible ways to do this:
 *  <ul>
 *  <li> If you ran our autoinstaller, use the commands
 *  {@code javac-introcs} and {@code java-introcs} (or {@code javac-algs4}
 *  and {@code java-algs4}) when compiling and executing. These commands
 *  add {@code stdlib.jar} (or {@code algs4.jar}) to the Java classpath, which
 *  provides access to {@code StdAudio}.
 *  <li> Download <a href = "https://introcs.cs.princeton.edu/java/code/stdlib.jar">stdlib.jar</a>
 *  (or <a href = "https://algs4.cs.princeton.edu/code/algs4.jar">algs4.jar</a>)
 *  and add it to the Java classpath.
 *  <li> Download <a href = "https://introcs.cs.princeton.edu/java/stdlib/StdAudio.java">StdAudio.java</a>
 *  and put it in the working directory.
 *  </ul>
 *  <p>
 *  As a test, cut-and-paste the following short program into your editor:
 *  <pre>
 *   public class TestStdAudio {
 *       public static void main(String[] args) {
 *           double freq = 440.0;
 *           for (int i = 0; i &lt; StdAudio.SAMPLE_RATE; i++) {
 *               double sample = 0.5 * Math.sin(2 * Math.PI * freq * i / StdAudio.SAMPLE_RATE);
 *               StdAudio.play(sample);
 *           }
 *           StdAudio.drain();
 *       }
 *   }
 *  </pre>
 *  <p>
 *  If you compile and execute the program, you should hear a pure tone
 *  whose frequency is concert A (440 Hz).
 *
 *  <p>
 *  <b>Playing audio samples.</b>
 *  You can use the following two methods to play individual audio samples:
 *  <ul>
 *  <li> {@link #play(double sample)}
 *  <li> {@link #play(double[] samples)}
 *  </ul>
 *  <p>
 *  Each method sends the specified sample (or samples) to the sound card.
 *  The individual samples are real numbers between –1.0 and +1.0. If a
 *  sample is outside this range, it will be <em>clipped</em> (rounded to
 *  –1.0 or +1.0). The samples are played in real time using a sampling
 *  rate of 44,100 Hz.
 *
 *  <p>
 *  <b>Playing audio files.</b>
 *  You can use the following method to play an audio file:
 *  <ul>
 *  <li> {@link #play(String filename)}
 *  </ul>
 *  <p>
 *  It plays an audio file (in WAVE, AU, AIFF, or MIDI format) and does
 *  not return until the audio file is finished playing. This can produce
 *  particularly striking programs with minimal code.
 *  For example, the following code fragment plays a drum loop:
 *
 *  <pre>
 *   while (true) {
 *       StdAudio.play("BassDrum.wav");
 *       StdAudio.play("SnareDrum.wav");
 *   }
 *  </pre>
 *
 *  The individual audio files
 *  (such as <a href = "https://introcs.cs.princeton.edu/java/stdlib/BassDrum.wav">BassDrum.wav</a>
 *  and <a href = "https://introcs.cs.princeton.edu/java/stdlib/SnareDrum.wav">SnareDrum.wav</a>)
 *  must be accessible to Java, typically
 *  by being in the same directory as the {@code .class} file.
 *  <p>
 *
 *  <b>Reading and writing audio files.</b>
 *  You can read and write audio files using the following two methods:
 *  <ul>
 *  <li> {@link #read(String filename)}
 *  <li> {@link #save(String filename, double[] samples)}
 *  </ul>
 *  <p>
 *  The first method reads audio samples from an audio file
 *  (in WAVE, AU, AIFF, or MIDI format)
 *  and returns them as a double array with values between –1.0 and +1.0.
 *  The second method saves the samples in the specified double array to an
 *  audio file (in WAVE, AU, or AIFF format).
 *
 *  <p>
 *  <b>Audio file formats.</b>
 *  {@code StdAudio} relies on the
 *  <a href = "https://www.oracle.com/java/technologies/javase/jmf-211-formats.html">Java Media Framework</a>
 *  for reading, writing, and playing  audio files. You should be able to read or play files
 *  in WAVE, AU, AIFF, and MIDI formats and save them to WAVE, AU, and AIFF formats.
 *  The file extensions corresponding to WAVE, AU, AIFF, and MIDI files
 *  are {@code .wav}, {@code .au}, {@code .aiff}, and {@code .midi},
 *  respectively.
 *  Some systems support additional audio file formats, but probably not MP3 or M4A.
 *  <p>
 *  The Java Media Framework supports a variety of different <em>audio data formats</em>,
 *  which includes
 *  <ul>
 *  <Li> the sampling rate (e.g., 44,100 Hz);
 *  <li> the number of bits per sample per channel (e.g., 8-bit or 16-bit);
 *  <li> the number of channels (e.g., monaural or stereo);
 *  <li> the byte ordering (e.g., little endian or big endian); and
 *  <li> the encoding scheme (typically linear PCM).
 *  </ul>
 *  <p>
 *  When saving files, {@code StdAudio} uses a sampling rate of 44,100 Hz,
 *  16 bits per sample, monaural audio, little endian, and linear PCM encoding.
 *  When reading files, {@code StdAudio} converts to a sammpling rate of 44,100 Hz,
 *  with 16 bits per sample.
 *
 *  <p>
 *  <b>Recording audio.</b>
 *  You can use the following methods to record audio samples that are
 *  played as a result of calls to {@link #play(double sample)} or
 *  {@link #play(double[] samples)}. To record audio samples that are
 *  played as a result of calls to {@link #play(String filename)},
 *  first read them into an array using {@link #read(String filename)}
 *  and call {@link #play(double[] samples)} on that array.
 *  <ul>
 *  <li> {@link #startRecording()}
 *  <li> {@link #stopRecording()}
 *  </ul>
 *  <p>
 *  The method {@link #startRecording()} begins recording audio.
 *  The method {@link #stopRecording()} stops recording and returns the recorded
 *  samples as an array of doubles.
 *  <p>
 *  {@code StdAudio} does not currently support recording audio that calls
 *  either {@link #play(String filename)} or
 *  {@link #playInBackground(String filename)}, as these may use different
 *  data formats, such as 8-bit and stereo.
 *  <p>
 *  <b>Playing audio files in a background thread.</b>
 *  You can use the following methods to play an audio file in a background thread
 *  (e.g., as a background score in your program).
 *  <ul>
 *  <li> {@link #playInBackground(String filename)}
 *  <li> {@link #stopInBackground()}
 *  </ul>
 *  <p>
 *  Each call to the first method plays the specified sound in a separate background
 *  thread. Unlike with the {@code play()} methods, your program will not wait
 *  for the samples to finish playing before continuing.
 *  It supports playing an audio file in WAVE, AU, AIFF, or MIDI format.
 *  It is possible to play
 *  multiple audio files simultaneously (in separate background threads).
 *  The second method stops the playing of all audio in background threads.
 *  <p>
 *  <b>Draining standard audio.</b>
 *  On some systems, your Java program may terminate before all of the samples have been
 *  sent to the sound card. To prevent this, it is recommend that you call the
 *  following method to indicate that you are done using standard audio:
 *  <ul>
 *  <li> {@link #drain()}
 *  </ul>
 *  <p>
 *  The method drains any samples queued to the sound card that have not yet been
 *  sent to the sound card.
 *  <p>
 *  <b>Reference.</b>
 *  For additional documentation,
 *  see <a href="https://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 *  <em>Computer Science: An Interdisciplinary Approach</em>
 *  by Robert Sedgewick and Kevin Wayne.
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
    private static final int MAX_16_BIT = 32768;
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

    // queue of background Runnable objects
    private static LinkedList<BackgroundRunnable> backgroundRunnables = new LinkedList<>();

    // for recording audio
    private static QueueOfDoubles recordedSamples = null;
    private static boolean isRecording = false;

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

            // from URL (including jar file)
            URL url = new URL(filename);
            return AudioSystem.getAudioInputStream(url);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("could not read '" + filename + "'", e);
        }
        catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("file of unsupported audio file format: '" + filename + "'", e);
        }
    }

    /**
     * Sends any queued samples to the sound card.
     */
    public static void drain() {
        if (bufferSize > 0) {
            line.write(buffer, 0, bufferSize);
            bufferSize = 0;
        }
        line.drain();
    }


    /**
     * Closes standard audio.
     */
/*
    public static void close() {
        drain();
        line.stop();
    }
*/
    /**
     * Writes one sample (between –1.0 and +1.0) to standard audio.
     * If the sample is outside the range, it will be clipped
     * (rounded to –1.0 or +1.0).
     *
     * @param  sample the sample to play
     * @throws IllegalArgumentException if the sample is {@code Double.NaN}
     */
    public static void play(double sample) {
        if (Double.isNaN(sample)) throw new IllegalArgumentException("sample is NaN");

        // clip if outside [-1, +1]
        if (sample < -1.0) sample = -1.0;
        if (sample > +1.0) sample = +1.0;

        // save sample if recording
        if (isRecording) {
            recordedSamples.enqueue(sample);
        }

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
     * Writes the array of samples (between –1.0 and +1.0) to standard audio.
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
     * Plays an audio file (in WAVE, AU, AIFF, or MIDI format) and waits for it to finish.
     * The file extension must be either {@code .wav}, {@code .au},
     * or {@code .aiff}.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if unable to play {@code filename}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public static void play(String filename) {

        // may not work for streaming file formats
        if (isRecording) {
            double[] samples = read(filename);
            for (double sample : samples)
                recordedSamples.enqueue(sample);
        }

        AudioInputStream ais = getAudioInputStreamFromFile(filename);
        SourceDataLine line = null;
        int BUFFER_SIZE = 4096; // 4K buffer
        try {
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            byte[] samples = new byte[BUFFER_SIZE];
            int count;
            while ((count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                line.write(samples, 0, count);
            }
        }
        catch (IOException | LineUnavailableException e) {
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
     * Reads audio samples from a file (in WAVE, AU, AIFF, or MIDI format)
     * and returns them as a double array with values between –1.0 and +1.0.
     * The file extension must be either {@code .wav}, {@code .au},
     * or {@code .aiff}.
     *
     * @param  filename the name of the audio file
     * @return the array of samples
     */
    public static double[] read(String filename) {
        // 4K buffer (must be a multiple of 2 for mono or 4 for stereo)
        int READ_BUFFER_SIZE = 4096;

        // create AudioInputStream from file
        AudioInputStream fromAudioInputStream = getAudioInputStreamFromFile(filename);
        AudioFormat fromAudioFormat = fromAudioInputStream.getFormat();

        // normalize AudioInputStream to 44,100 Hz, 16-bit audio, mono, signed PCM, little endian
        // https://docs.oracle.com/javase/tutorial/sound/converters.html
        AudioFormat toAudioFormat = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, MONO, SIGNED, LITTLE_ENDIAN);
        if (!AudioSystem.isConversionSupported(toAudioFormat, fromAudioFormat)) {
            throw new IllegalArgumentException("system cannot convert from " + fromAudioFormat + " to " + toAudioFormat);
        }
        AudioInputStream toAudioInputStream = AudioSystem.getAudioInputStream(toAudioFormat, fromAudioInputStream);

        // extract the audio data and convert to a double[] with each sample between -1 and +1
        try {
            QueueOfDoubles queue = new QueueOfDoubles();
            byte[] bytes = new byte[READ_BUFFER_SIZE];
            int count;
            while ((count = toAudioInputStream.read(bytes, 0, READ_BUFFER_SIZE)) != -1) {

                // little endian, monoaural
                for (int i = 0; i < count/2; i++) {
                    double sample = ((short) (((bytes[2*i+1] & 0xFF) << 8) | (bytes[2*i] & 0xFF))) / ((double) MAX_16_BIT);
                    queue.enqueue(sample);
                }

                // little endian, stereo (perhaps, for a future version that supports stereo)
                /*
                for (int i = 0; i < count/4; i++) {
                    double left  = ((short) (((bytes[4*i + 1] & 0xFF) << 8) | (bytes[4*i + 0] & 0xFF))) / ((double) MAX_16_BIT);
                    double right = ((short) (((bytes[4*i + 3] & 0xFF) << 8) | (bytes[4*i + 2] & 0xFF))) / ((double) MAX_16_BIT);
                    double sample = (left + right) / 2.0;
                    queue.enqueue(sample);
                }
                */
            }
            toAudioInputStream.close();
            fromAudioInputStream.close();
            return queue.toArray();
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("could not read '" + filename + "'", ioe);
        }
    }

    /**
     * Saves the double array as an audio file (using WAV, AU, or AIFF format).
     * The file extension must be either {@code .wav}, {@code .au},
     * or {@code .aiff}.
     * The format uses a sampling rate of 44,100 Hz, 16-bit audio,
     * mono, signed PCM, ands little Endian.
     *
     * @param  filename the name of the audio file
     * @param  samples the array of samples
     * @throws IllegalArgumentException if unable to save {@code filename}
     * @throws IllegalArgumentException if {@code samples} is {@code null}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @throws IllegalArgumentException if {@code filename} extension is not
     *         {@code .wav}, {@code .au}, or {@code .aiff}.
     */
    public static void save(String filename, double[] samples) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
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
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, samples.length)) {

            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, ais)) {
                    throw new IllegalArgumentException("saving to WAVE file format is not supported on this system");
                }
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            }
            else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.AU, ais)) {
                    throw new IllegalArgumentException("saving to AU file format is not supported on this system");
                }
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            }
            else if (filename.endsWith(".aif") || filename.endsWith(".aiff") || filename.endsWith(".AIF") || filename.endsWith(".AIFF")) {
                if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.AIFF, ais)) {
                    throw new IllegalArgumentException("saving to AIFF file format is not supported on this system");
                }
                AudioSystem.write(ais, AudioFileFormat.Type.AIFF, new File(filename));
            }
            else {
                throw new IllegalArgumentException("file extension for saving must be .wav, .au, or .aif");
            }
        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("unable to save file '" + filename + "'", ioe);
        }
    }

    /**
     * Stops the playing of all audio files in background threads.
     */
    public static synchronized void stopInBackground() {
        for (BackgroundRunnable runnable : backgroundRunnables) {
            runnable.stop();
        }
        backgroundRunnables = new LinkedList<>();
    }

    /**
     * Plays an audio file (in WAVE, AU, AIFF, or MIDI format) in its own
     * background thread. Multiple audio files can be played simultaneously.
     * The file extension must be either {@code .wav}, {@code .au},
     * or {@code .aiff}.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if unable to play {@code filename}
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     */
    public static synchronized void playInBackground(final String filename) {
        BackgroundRunnable runnable = new BackgroundRunnable(filename);
        new Thread(runnable).start();
        backgroundRunnables.add(runnable);
    }

    private static class BackgroundRunnable implements Runnable {
        private volatile boolean exit = false;
        private final String filename;

        public BackgroundRunnable(String filename) {
            this.filename = filename;
        }

        // https://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
        // play a wav or aif file
        // javax.sound.sampled.Clip fails for long clips (on some systems)
        public void run() {
            AudioInputStream ais = getAudioInputStreamFromFile(filename);

            SourceDataLine line = null;
            int BUFFER_SIZE = 4096; // 4K buffer

            try {
                AudioFormat audioFormat = ais.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(audioFormat);
                line.start();
                byte[] samples = new byte[BUFFER_SIZE];
                int count;
                while (!exit && (count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                    line.write(samples, 0, count);
                }
            }
            catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
            finally {
                if (line != null) {
                    line.drain();
                    line.close();
                }
            }
        }

        public void stop() {
            exit = true;
        }
    }


    /**
     * Loops an audio file (in WAVE, AU, AIFF, or MIDI format) in its
     * own background thread.
     *
     * @param filename the name of the audio file
     * @throws IllegalArgumentException if {@code filename} is {@code null}
     * @deprecated to be removed in a future update, as it doesn't interact
     *             well with {@link #playInBackground(String filename)} or
     *             {@link #stopInBackground()}.
     */
    @Deprecated
    public static synchronized void loopInBackground(String filename) {
        if (filename == null) throw new IllegalArgumentException();

        final AudioInputStream ais = getAudioInputStreamFromFile(filename);

        try {
            Clip clip = AudioSystem.getClip();
            // Clip clip = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (IOException | LineUnavailableException e) {
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


    /**
     * Turns on audio recording.
     */
    public static void startRecording() {
        if (!isRecording) {
            recordedSamples = new QueueOfDoubles();
            isRecording = true;
         }
         else {
             throw new IllegalStateException("startRecording() must not be called twice in a row");
         }
    }

    /**
     * Turns off audio recording and returns the recorded samples.
     * @return the array of recorded samples
     */
    public static double[] stopRecording() {
        if (isRecording) {
            double[] results = recordedSamples.toArray();
            isRecording = false;
            recordedSamples = null;
            return results;
         }
         else {
             throw new IllegalStateException("stopRecording() must be called after calling startRecording()");
         }
    }


   /***************************************************************************
    * Helper class for reading and recording audio.
    ***************************************************************************/
    private static class QueueOfDoubles {
        private static final int INIT_CAPACITY = 16;
        private double[] a;   // array of doubles
        private int n;        // number of items in queue

        // create an empty queue
        public QueueOfDoubles() {
            a = new double[INIT_CAPACITY];
            n = 0;
        }

        // resize the underlying array holding the items
        private void resize(int capacity) {
            assert capacity >= n;
            double[] temp = new double[capacity];
            for (int i = 0; i < n; i++)
                temp[i] = a[i];
            a = temp;
        }

        // enqueue item onto the queue
        public void enqueue(double item) {
            if (n == a.length) resize(2*a.length);    // double length of array if necessary
            a[n++] = item;                            // add item
        }


        // number of items in queue
        public int size() {
            return n;
        }

        // return the items as an array of length n
        public double[] toArray() {
            double[] result = new double[n];
            for (int i = 0; i < n; i++)
                result[i] = a[i];
            return result;
        }

    }


    /**
     * Test client - plays some sound files and concert A.
     *
     * @param args the command-line arguments (none should be specified)
     */
    public static void main(String[] args) {
        // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i <= StdAudio.SAMPLE_RATE; i++) {
            StdAudio.play(0.5 * Math.sin(2*Math.PI * freq * i / StdAudio.SAMPLE_RATE));
        }


        String base = "https://introcs.cs.princeton.edu/java/stdlib/";

        // play some sound files
        StdAudio.play(base + "test.wav");          // helicopter
        StdAudio.play(base + "test-22050.wav");    // twenty-four
        StdAudio.play(base + "test.midi");         // a Mozart measure

        // a sound loop
        for (int i = 0; i < 10; i++) {
            StdAudio.play(base + "BaseDrum.wav");
            StdAudio.play(base + "SnareDrum.wav");
        }

        // need to call this in non-interactive stuff so the program doesn't terminate
        // until all the sound leaves the speaker.
        StdAudio.drain();
    }
}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
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
