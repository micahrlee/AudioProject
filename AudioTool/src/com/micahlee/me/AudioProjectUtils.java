package com.micahlee.me;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;

/**
 * Made by Micah Lee for University of Washington CSS457. 
 * @author Micah Lee - micahrlee@gmail.com
 *
 */
public class AudioProjectUtils {
	
	public static void inform(String message, String title){
		if(message != null && title != null){
			JOptionPane.showMessageDialog(null, message, title ,JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public static void error(String message, Thread thread){
		if(message != null){
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.OK_OPTION);
		}
		if(thread != null){
			thread = null;
		}
	}
	
	public static File bytesToWav(byte[] b){
		File f = null;
		try {
			f = File.createTempFile("sound", ".wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			AudioRecordingWindow.audioInputStream.reset();
			AudioSystem.write(AudioRecordingWindow.audioInputStream, AudioFileFormat.Type.WAVE, f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
}
