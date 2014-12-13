package com.micahlee.me;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
 
/**
 *
 * @author Adam
 */
public class FFTPlotter implements Runnable
{
    public  File file;
    public  WavFile audio;
    public Thread thread;
        
    //Create head window and iterator
    private  FFTWindow head;
    private  FFTWindow previous;
    
    private  int readCount;
    private boolean fft;
    
    private FFTWindow windowedSignal;
    private  double[][] rawSignal;
    private double[] magFFT;
    
    //Constants
    public static final int WINDOW_SIZE = 1024;
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
            magFFT = fft.getMagFFT(windowedSignal.windowedData());
            return magFFT;
        }
        catch (WavFileException | IOException e)
        {
            return null;
        }
    }
    
    public double[] getSignalData()
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
            magFFT = fft.getMagFFT(windowedSignal.windowedData());
            return rawSignal[0];
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
        windowedSignal.rawData(rawSignal[0]);
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
            windowedSignal.rawData(rawSignal[0]);
        }
        else
            rawSignal = null;
    }
    
    public boolean hasStarted = false;

    public void start(File f, boolean fft){
    	file = f;
    	this.fft = fft;
    	this.windowedSignal = new FFTWindow();
    	thread = new Thread(this);
    	thread.start();
    }
	@Override
	public void run() {
        DiscretePlotter plot = new DiscretePlotter(this, fft);
        plot.showPlot();
	}
}
