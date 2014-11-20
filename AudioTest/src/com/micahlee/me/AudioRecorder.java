package com.micahlee.me;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;


public class AudioRecorder extends Thread{
	public static final AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, true);
	private TargetDataLine dataLine;
//	private SourceDataLine playBackLine;
	private byte[] data;
	private boolean stop = true;
	
	public AudioRecorder(){
		initDataLine();
//		initPlayBack();
		stop = true;
	}
	
	private void initDataLine(){
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)){
			System.out.println("Please use a microphone.");
			System.exit(0);
		}
		try{
			dataLine = (TargetDataLine) AudioSystem.getLine(info);
			dataLine.open(format);
		}
		catch(LineUnavailableException e){
			System.out.println("Could not obtain audio line.");
		}
	}
	
//	private void initPlayBack(){
//		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
//		try {
//			playBackLine = (SourceDataLine) AudioSystem.getLine(info);
//			playBackLine.open();
//		} catch (LineUnavailableException e) {
//			System.out.println("Could not obtain audio line.");
//		}
//	}
	
	public void listen(){
		stop = false;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		data = new byte[4096];
		//This is where we begin our audio capture
		dataLine.start();
		//This is where we store our data
		int bytesRead;
		while(!stop){
			System.out.println("Recordingggggg");
			bytesRead = dataLine.read(data, 0, data.length);
			out.write(data, 0, bytesRead);
		}
	}
	
	public void stopRecord(){
		stop = true;
		dataLine.stop();
		dataLine.close();
	}
	
	public void playBack(){
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		try {
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.open(format, data, 0, data.length);
			clip.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void filterOrWhatever(){
		
	}

	@Override
	public void run() {
		listen();
		playBack();
		filterOrWhatever();
	}
}
