package fftplotter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Arrays;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Adam
 */
public class DiscretePlotter extends JFrame 
{
    //Interface Methods
    public DiscretePlotter(FFTPlotter plotter)
    {
        mPlotter = plotter;
        
        mData = new LinkedList<>();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 600));
        
        JPanel buttonsPanel = new JPanel();
        
        mNextButton = new JButton("Show Next");
        mNextButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                showPlot();
            }
        });
        buttonsPanel.add(mNextButton);
        
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        
        this.add(new PlotRenderer());
        
        pack();
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
    
    private class PlotRenderer extends JComponent
    {
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            Point offset = new Point(50, 300);
            Point2D.Float pointStep = new Point2D.Float((float) 900 / mData.size(), 50);


            Point previous = new Point();
            Point current = new Point();

            int count = 0;
            for (Double data : mData)
            {
                if (count == 0)
                {
                    previous.x = offset.x + (int) (count * pointStep.x);
                    previous.y = offset.y + (int) (data * pointStep.y);

                    count++;

                    continue;
                }

                current.x = offset.x + (int) (count * pointStep.x);
                current.y = offset.y + (int) (data * pointStep.y);

                g.setColor(Color.BLUE);
                g.drawLine(previous.x, previous.y, current.x, current.y);

                previous.setLocation(current);
                count++;
            }
        } 
    }
    
    public void showPlot()
    {
        setVisible(true);
        
        mData.clear();
        double[] readCount = mPlotter.getFFTData();
        
        if (readCount != null)
        {
            addDiscrete(readCount);
            repaint();
        }
        else
            mNextButton.setEnabled(false);
    }
    
    //Private Methods
    
    //Private Members
    private LinkedList<Double> mData;
    
    private FFTPlotter mPlotter;
    
    private JButton mNextButton;
}
