package fftplotter;

import java.lang.Math;
import java.util.Arrays;

/**
 *
 * @author Adam
 */
public class FFTWindow {
    
    private double[] mRawData;
    private double[] mWindowedData;
    
    public FFTWindow nextWindow;
    
    private void applyWindow()
    {
        mWindowedData = new double[mRawData.length];
        
        for (int i = 0; i < mRawData.length; i++)
        {
            //The Hamming window algorith
            mWindowedData[i] = 
                    mRawData[i] * 
                        (.54 - .46 * Math.cos(2 * Math.PI * i / (mRawData.length - 1)));
        }
    }
    
    public double[] rawData(double[] data)
    {
        if (data == null)
            return mRawData;
        
        mRawData = Arrays.copyOf(data, data.length);
        applyWindow();
        
        return mRawData;
    }
    
    public double[] windowedData()
    {
        return mWindowedData;
    }
}
