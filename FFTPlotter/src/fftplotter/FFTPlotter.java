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
   
    public static final int WINDOW_SIZE = 128;
    public static final int FFT_STEP = WINDOW_SIZE / 4;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, WavFileException
    {
        File file = new File("test.wav");
        WavFile audio = WavFile.openWavFile(file);
        
        //Create head window and iterator
        FFTWindow head = new FFTWindow();
        FFTWindow previous = null;
        
        //Get individual windows
        int readCount = 0;
        int lastIndex = 0;
        double[][] rawSignal = new double[audio.getNumChannels()][WINDOW_SIZE];
        for (int i = 0; i < rawSignal.length; i++)
            Arrays.fill(rawSignal[i], 0);
        
        //Assume we have at least one window size of data available ~1/80 of a sec
        readCount = audio.readFrames(rawSignal, lastIndex, WINDOW_SIZE);
        
        while (readCount > 0)
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
            
            
            lastIndex += FFT_STEP;
            
            if (lastIndex >= 10000)
            {
                //Test code
                DiscretePlotter plot = new DiscretePlotter();
                plot.addDiscrete(rawSignal[1]);
                plot.showPlot();

                break;
            }
        }
    }
    
}
