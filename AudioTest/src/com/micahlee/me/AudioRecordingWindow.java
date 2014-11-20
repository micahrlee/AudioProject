package com.micahlee.me;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

public class AudioRecordingWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton record;
	private JButton stopRecord;
	private JSpinner fileExtenstion;
	private JCheckBox saveFile;
	private AudioRecorder ar;
	private static final String fileName = "audio";

	public AudioRecordingWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 213, 103);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		record = new JButton("Record");
		
		stopRecord = new JButton("Stop Recording");
		stopRecord.setEnabled(false);
		setLocationRelativeTo(null);
		
		saveFile = new JCheckBox("Save File");
		saveFile.setSelected(true);
		
		fileExtenstion = new JSpinner();
		fileExtenstion.setModel(new SpinnerListModel(new String[] {"WAV", "AIFF", "AIFC", "AU", "SND"}));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(fileExtenstion, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
						.addComponent(record, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(saveFile)
						.addComponent(stopRecord)))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(saveFile)
						.addComponent(fileExtenstion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(record)
						.addComponent(stopRecord)))
		);
		contentPane.setLayout(gl_contentPane);
		activateListeners();
	}
	
	private void activateListeners(){
		record.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AudioFileFormat.Type type = null;
				switch((String)fileExtenstion.getValue()){
					case "WAV":
						type = AudioFileFormat.Type.WAVE;
						break;
					case "AIFF":
						type = AudioFileFormat.Type.AIFF;
						break;
					case "AIFC":
						type = AudioFileFormat.Type.AIFC;
						break;
					case "AU":
						type = AudioFileFormat.Type.AU;
						break;
					case "SND":
						type = AudioFileFormat.Type.SND;
						break;
						
				}
				ar = new AudioRecorder(getPath() + getFileName(), type, saveFile.isSelected());
				record.setEnabled(false);
				stopRecord.setEnabled(true);
				ar.start();
			}
		});
		
		stopRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				record.setEnabled(true);
				stopRecord.setEnabled(false);
				ar.stopRecord();
			}
		});
	}
	
	private String getFileName(){
		File testFile = new File(getPath() + File.separator);
		ArrayList<File> list = new ArrayList<File>(Arrays.asList(testFile.listFiles()));
		int append = 0;
		if(!contains(list, Pattern.compile("audio\\..*"))){
			return new String(fileName + "." + ((String) fileExtenstion.getValue()).toLowerCase());
		}
		while(contains(list, Pattern.compile("audio" + append + "\\..*"))){
			++append;
		}
		
		return new String(fileName + append + "." +((String) fileExtenstion.getValue()).toLowerCase());
	}
	
	private boolean contains(ArrayList<File> al, Pattern p){
		for(File f : al){
			if(p.matcher(f.getName()).matches()){
				return true;
			}
		}
		return false;
	}
	
	private String getPath(){
		return System.getProperty("user.dir") + File.separator;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AudioRecordingWindow frame = new AudioRecordingWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
