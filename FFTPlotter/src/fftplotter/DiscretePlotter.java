package fftplotter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Point;
import java.util.Arrays;

/**
 *
 * @author Adam
 */
public class DiscretePlotter extends JComponent 
{
    //Interface Methods
    public DiscretePlotter()
    {
        mData = new LinkedList<>();
    }
    
    public void addDiscrete(Double data)
    {
        mData.add(data);
    }
    
    public void addDiscrete(double[] data)
    {
        for (int i = 0; i < data.length; i++)
            mData.add(data[i]);
    }
    
    public void addDiscrete(Double[] data)
    {
        mData.addAll(Arrays.asList(data));
    }
    
    public int getLength()
    {
        return mData.size();
    }
    
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Point offset = new Point(50, 300);
        Point pointStep = new Point(900 / mData.size(), 500);
        
        
        Point previous = new Point();
        Point current = new Point();
        
        int count = 0;
        for (Double data : mData)
        {
            if (count == 0)
            {
                previous.x = offset.x + count * pointStep.x;
                previous.y = offset.y + (int) (data * pointStep.y);
                
                count++;
                
                continue;
            }
            
            current.x = offset.x + count * pointStep.x;
            current.y = offset.y + (int) (data * pointStep.y);
            
            g.setColor(Color.BLUE);
            g.drawLine(previous.x, previous.y, current.x, current.y);
            
            previous.setLocation(current);
            count++;
        }
    }
    
    public void showPlot()
    {
        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 600));
        testFrame.getContentPane().add(this, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        testFrame.pack();
        testFrame.setVisible(true);
        
        repaint();
    }
    
    //Private Methods
    
    //Private Members
    LinkedList<Double> mData;
}
