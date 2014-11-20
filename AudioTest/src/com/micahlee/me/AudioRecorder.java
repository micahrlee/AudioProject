package com.micahlee.me;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

public class AudioRecorder extends Thread{
	public static final AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
	public AudioFileFormat.Type type;
	public static final int bufSize = 4096;
	private TargetDataLine dataLine;
	private byte[] data;
	private final String path;
	private final boolean saveFile;
	private boolean stop = true;
	
	public AudioRecorder(final String path, final AudioFileFormat.Type type, final boolean saveFile){
		initDataLine();
		stop = true;
		if(type != null){
			this.type = type;
		}
		else{
			this.type = AudioFileFormat.Type.WAVE;
		}
		this.path = path;
		this.saveFile = saveFile;
	}
	
	private void initDataLine(){
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format, bufSize);
		if(!AudioSystem.isLineSupported(info)){
			JOptionPane.showMessageDialog(null, "Could not find microphone.\n", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		try{
			dataLine = (TargetDataLine) AudioSystem.getLine(info);
			dataLine.open(format);
		}
		catch(LineUnavailableException e){
			JOptionPane.showMessageDialog(null, "Could not obtain recording line.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void listen(){
		System.out.println("Recording.");
		stop = false;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		data = new byte[bufSize];
		int bytesRead;
		//This is where we begin our audio capture
		dataLine.start();
		//This is where we store our data
		while(!stop){
			bytesRead = dataLine.read(data, 0, data.length);
			out.write(data, 0, bytesRead);
		}
		if(saveFile){
			saveFile();
		}
	}
	
	public void stopRecord(){
		stop = true;
		dataLine.stop();
		dataLine.close();
	}
	
	private void saveFile(){
		InputStream audioData = new ByteArrayInputStream(data);
		AudioInputStream ais = new AudioInputStream(audioData, format, (long)data.length);
		try {
			AudioSystem.write(ais, type, new File(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "File saved successfully!\n Open directory?", "Open Directory?", JOptionPane.YES_NO_OPTION)){
			try {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir") + File.separator));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void playBack(){
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		try {
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(format, data, 0, data.length);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void filterOrWhatever(){
		
	}

	@Override
	public void run() {
		listen();
		playBack();
		filterOrWhatever();
	}
}
