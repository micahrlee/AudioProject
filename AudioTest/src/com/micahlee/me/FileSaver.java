package com.micahlee.me;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JDialog;

public class FileSaver implements Runnable {
	public Thread thread;
	private AudioFileFormat.Type type;
	public static String fileName;
	
	public FileSaver(){
		getPath();
	}
	
	public void start(AudioFileFormat.Type type) {
		thread = new Thread(this);
		this.type = type;
		thread.start();
	}

	@Override
	public void run() {
		try {
			FileNameWindow dialog = new FileNameWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(getPath() + appendIfExists(fileName));
		createAudio(file);
		AudioProjectUtils.inform("Recording of " + file.getName() + " has finished.", "Done");
	}	
	
	private void createAudio(File f){
		try {
			AudioRecordingWindow.audioInputStream.reset();
			AudioSystem.write(AudioRecordingWindow.audioInputStream, type, f);
		} catch (IOException e) {
			AudioProjectUtils.error("Error writing file", thread);
			return;
		}
	}
	
	private String appendIfExists(String f){
		File test = new File(getPath() + f + "." + type.getExtension().toLowerCase());
		if(test.exists()){
			int append = 0;
			while(test.exists()){
				test = new File(getPath() + fileName + append + "." + type.getExtension().toLowerCase());
				++append;
			}
			return fileName + (append - 1) + "." + type.getExtension().toLowerCase();
		}
		return fileName + "." + type.getExtension().toLowerCase();
	}
	
	private String getPath() {
		if (!new File(System.getProperty("user.dir") + File.separator + "Audio"	+ File.separator).exists()) {
			new File(System.getProperty("user.dir") + File.separator + "Audio" + File.separator).mkdir();
		}
		return System.getProperty("user.dir") + File.separator + "Audio" + File.separator;
	}

}
