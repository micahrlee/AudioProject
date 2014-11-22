package com.micahlee.me;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Playback implements Runnable {
	public static final AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
	public static final float rate = 44100.0f;
	public static final int channels = 2;
	public static final int frameSize = 4;
	public static final int sampleSize = 16;
	public static final boolean bigEndian = true;
	public static final AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	private static final int bufSize = 16384;
	private File file;
	private SourceDataLine line;
	private AudioInputStream playback;
	private Clip clip = null;
	public Thread thread;

	public void start() {
		thread = new Thread(this);
		thread.start();
		file = null;
	}
	
	public void start(File f){
		thread = new Thread(this);
		thread.start();
		file = f;
	}

	public void stop() {
		thread = null;
		if(clip != null){
			clip.stop();
		}
	}
	
	private boolean setupPlayback(){
		if (AudioRecordingWindow.audioInputStream == null) {
			AudioProjectUtils.error("No audio to play back", thread);
			return false;
		}
		try {
			AudioRecordingWindow.audioInputStream.reset();
		} catch (Exception e) {
			AudioProjectUtils.error("Unable to reset the stream\n" + e, thread);
			return false;
		}

		playback = AudioSystem.getAudioInputStream(format, AudioRecordingWindow.audioInputStream);

		if (playback == null) {
			AudioProjectUtils.error("Format error", thread);
			return false;
		}

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			AudioProjectUtils.error(info + " not supported.", thread);
			return false;
		}

		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufSize);
		} catch (LineUnavailableException ex) {
			AudioProjectUtils.error("Line Unavailable", thread);
			return false;
		}
		return true;
	}
	
	private boolean playback(File f){
		if(f == null){
			return false;
		}
		try {
			AudioRecordingWindow.audioInputStream = AudioSystem.getAudioInputStream(f);
		} catch (UnsupportedAudioFileException e) {
			AudioProjectUtils.error("Unsupported file type: " + f.getName(), thread);
			return false;
		} catch (IOException e) {
			AudioProjectUtils.error("Error opening " + f.getName(), thread);
			return false;
		}
		AudioFormat aFormat = AudioRecordingWindow.audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, aFormat);
		try {
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(AudioRecordingWindow.audioInputStream);
		} catch (LineUnavailableException e1) {
			AudioProjectUtils.error("Line unavailable", thread);
			return false;
		} catch (IOException e) {
			AudioProjectUtils.error("Error opening " + f.getName(), thread);
			return false;
		}
		clip.start();
		clip.addLineListener(new LineListener(){
			@Override
			public void update(LineEvent e) {
				if(e.getType() == LineEvent.Type.STOP){
					clip.close();
				}		
			}
		});
		while(clip.isOpen());
		return true;
	}
	
	private boolean playback(){
		int frameSize = format.getFrameSize();
		int bufLength = (line.getBufferSize() / 8) * frameSize;
		byte[] data = new byte[bufLength];
		int bytesRead = 0;
		line.start();
		while (thread != null) {
			try {
				if ((bytesRead = playback.read(data)) == -1) {
					break;
				}
				int bytesRemaining = bytesRead;
				while (bytesRemaining > 0) {
					bytesRemaining -= line.write(data, 0, bytesRemaining);
				}
			} catch (Exception e) {
				AudioProjectUtils.error("Error during playback", thread);
				return false;
			}
		}
		clearLine();
		return true;
	}
	
	private void clearLine(){
		if (thread != null) {
			line.drain();
		}
		line.stop();
		line.close();
		line = null;
	}

	public void run() {
		if(file == null){ 
			if(setupPlayback()){
				if(playback()){
					AudioProjectUtils.error(null, thread);
				}
			}
		}
		else{
			if(playback(file));
		}
		AudioRecordingWindow.startPlay.setText("Play");
	}
}
