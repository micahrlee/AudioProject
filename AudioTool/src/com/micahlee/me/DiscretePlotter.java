package com.micahlee.me;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSeparator;
/**
 *
 * @author Adam
 */
public class DiscretePlotter extends JFrame 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JLabel begTime;
	public JLabel endTime;
	private DecimalFormat df = new DecimalFormat("0.0000");
	private int count;

	//Interface Methods
    public DiscretePlotter(final FFTPlotter plotter, boolean fft)
    {
    	mIsFFT = fft;
    	setTitle("Plot");
        mPlotter = plotter;
        
        mData = new LinkedList<>();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(1000, 600));
        
        JPanel buttonsPanel = new JPanel();
        count = 0;
        mNextButton = new JButton("Show Next");
        mNextButton.addActionListener(new ActionListener(){        	
            public void actionPerformed(ActionEvent e){
            	String beg = String.format("%.4f", (count * (((double) FFTPlotter.FFT_STEP) / 44100)));
            	String end = df.format(((double)FFTPlotter.WINDOW_SIZE / 44100 + count * (double)(((double) FFTPlotter.FFT_STEP) / 44100)));
            	begTime.setText(beg + " seconds");
            	endTime.setText(end + " seconds");
                showPlot();
                count++;
            }
        });
       
        //If the user tries to exit the window, a prompt will show to prevent any unwanted activity
  		addWindowListener(new WindowAdapter() {
  			public void windowClosing(WindowEvent e) {
  				plotter.hasStarted = false;
  			}
  		});
        
        begTime = new JLabel("Beg Time");
        
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        
        endTime = new JLabel("End Time");
        String beg = String.format("%.4f", (count * (((double) FFTPlotter.FFT_STEP) / 44100)));
    	String end = df.format(((double)FFTPlotter.WINDOW_SIZE / 44100 + count * (double)(((double) FFTPlotter.FFT_STEP) / 44100)));
        begTime.setText(beg + " seconds");
    	endTime.setText(end + " seconds");
    	count++;
        JSeparator separator = new JSeparator();
        GroupLayout gl_buttonsPanel = new GroupLayout(buttonsPanel);
        gl_buttonsPanel.setHorizontalGroup(
        	gl_buttonsPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_buttonsPanel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_buttonsPanel.createParallelGroup(Alignment.LEADING)
        				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
        				.addGroup(gl_buttonsPanel.createSequentialGroup()
        					.addComponent(begTime)
        					.addGap(370)
        					.addComponent(mNextButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addGap(359)
        					.addComponent(endTime)
        					.addGap(64)))
        			.addContainerGap())
        );
        gl_buttonsPanel.setVerticalGroup(
        	gl_buttonsPanel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_buttonsPanel.createSequentialGroup()
        			.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
        			.addGap(9)
        			.addGroup(gl_buttonsPanel.createParallelGroup(Alignment.BASELINE)
        				.addComponent(mNextButton)
        				.addComponent(begTime)
        				.addComponent(endTime))
        			.addContainerGap())
        );
        buttonsPanel.setLayout(gl_buttonsPanel);
        
        getContentPane().add(new PlotRenderer());
        
        pack();
    }
    
//    private boolean isMeaningful(){
//    	int nextByte = (int)Math.log(mData.size());
//    	double sum = 0;
//    	for(int i = 0; i < mData.size(); i+= nextByte){
//    		sum += Math.abs(mData.get(i));
//    	}
//    	return Math.round((sum/(mData.size()/nextByte)) + 0.4) != 0;
//    }
    
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
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
        double[] data = null;
        mData.clear();
        if(mIsFFT){
        	data = mPlotter.getFFTData();
        }
        else{
        	data = mPlotter.getSignalData();
        }
        
        
        if (data != null)
        {
            addDiscrete(data);
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
    
    private boolean mIsFFT;
}
