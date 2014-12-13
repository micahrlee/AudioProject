package fftplotter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 *
 * @author Adam
 */
public class FFTPlotter
{
    public  File file;
    public  WavFile audio;
        
    //Create head window and iterator
    private  FFTWindow head;
    private  FFTWindow previous;
    
    private  int readCount;
    
    private  double[][] rawSignal;
    private double[] magFFT;
    
    //Constants
    public static final int WINDOW_SIZE = 512;
    public static final int FFT_STEP = WINDOW_SIZE / 4;
    
    public double[] getFFTData()
    {
        try
        {
            if (!hasStarted)
            {
                initialize(file);

                hasStarted = true;
            }
            else
                loadNextFFT();
            
            if (rawSignal == null)
                return null;
            
            FFT fft = new FFT();
            magFFT = fft.getMagFFT(rawSignal[0]);
            return magFFT;
        }
        catch (WavFileException | IOException e)
        {
            return null;
        }
    }
    
    public void initialize(File audioFile) throws WavFileException, IOException
    {
        file = audioFile;
        audio = WavFile.openWavFile(file);
        
        //Create head window and iterator
        head = new FFTWindow();
        previous = null;
        
        rawSignal = new double[audio.getNumChannels()][WINDOW_SIZE];
        for (int i = 0; i < rawSignal.length; i++)
            Arrays.fill(rawSignal[i], 0);
        
        //Assume we have at least one window size of data available ~1/80 of a sec
        readCount = audio.readFrames(rawSignal, 0, WINDOW_SIZE);
    }
    
    //Returns how many samples were read
    public void loadNextFFT() throws WavFileException, IOException
    {
        if (readCount != 0)
        {
            //Iterate the iterator
            if (previous == null) //Is this the first window
            {
                head.rawData(rawSignal[1]);
                
                previous = head;
            }
            else
            {
                previous.nextWindow = new FFTWindow();
                previous = previous.nextWindow;
                previous.rawData(rawSignal[1]);
            }
            
            //Shift all of the doubles -FFT_STEP indices
            for (int i = FFT_STEP; i < WINDOW_SIZE; i++)
                for (int j = 0; j < rawSignal.length; j++)
                    rawSignal[j][i - FFT_STEP] = rawSignal[j][i];
                
            readCount = audio.readFrames(rawSignal, WINDOW_SIZE - FFT_STEP, FFT_STEP);
        }
        else
            rawSignal = null;
    }
    
    private static boolean hasStarted = false;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, WavFileException
    {
        FFTPlotter temp = new FFTPlotter();
        temp.file = new File("test.wav");
        DiscretePlotter plot = new DiscretePlotter(temp);
        plot.showPlot();
    }
    
}
