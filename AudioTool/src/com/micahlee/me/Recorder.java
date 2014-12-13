package com.micahlee.me;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Made by Micah Lee for University of Washington CSS457. 
 * @author Micah Lee - micahrlee@gmail.com
 *
 */
public class Recorder implements Runnable {
	public static final AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
	public static final float rate = 44100.0f;
	public static final int channels = 2;
	public static final int frameSize = 4;
	public static final int sampleSize = 16;
	public static final boolean bigEndian = true;
	public static final AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize/8) * channels, rate, bigEndian);
	private TargetDataLine line;
	public Thread thread;
	private ByteArrayOutputStream out;
	
	public void start(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop(){
		thread = null;
	}
	
	private boolean setupRecorder(){
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if(!AudioSystem.isLineSupported(info)){
			AudioProjectUtils.error(info + " not supported", thread);
			return false;
		}
		
		try {
			line = (TargetDataLine)AudioSystem.getLine(info);
			line.open(format, line.getBufferSize());
		} catch (LineUnavailableException e) {
			AudioProjectUtils.error("Line Unavailable", thread);
			return false;
		}
		return true;
	}
	
	private void record(){
		out = new ByteArrayOutputStream();
		int frameSize = format.getFrameSize();
		int bufLength = (line.getBufferSize() / 8) * frameSize;
		AudioRecordingWindow.data = new byte[bufLength];
		int bytesRead;
		
		line.start();
		
		while(thread != null){
			if((bytesRead = line.read(AudioRecordingWindow.data, 0, bufLength)) == -1){
				break;
			}
			out.write(AudioRecordingWindow.data, 0, bytesRead);
		}
		clearLine();
		clearBuffer();
	}
	
	private void writeToStream(){
		byte audioBytes[] = out.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
		AudioRecordingWindow.audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSize);
		try {
			AudioRecordingWindow.audioInputStream.reset();
		} catch (Exception ex) {
			return;
		}
	}
	
	private void clearBuffer(){
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void clearLine(){
		line.stop();
		line.close();
		line = null;
	}
	
	@Override
	public void run() {
		if(setupRecorder()){
			record();
			writeToStream();
		}
		AudioRecordingWindow.setFile(null);
		AudioProjectUtils.error(null, thread);
	}

}
