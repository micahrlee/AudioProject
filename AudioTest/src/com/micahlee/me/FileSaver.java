package com.micahlee.me;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
		fileName = appendIfExists(fileName, "." + type.getExtension().toLowerCase());
		File file = new File(getPath() + fileName + "." + type.getExtension().toLowerCase());
		createAudio(file);
		writeBytes();
		AudioProjectUtils.inform("Recording of " + file.getName() + " has finished.", "Done");
	}	
	
	private void createAudio(File f){
		try {
			AudioRecordingWindow.audioInputStream.reset();
			AudioSystem.write(AudioRecordingWindow.audioInputStream, type, f);
		} catch (IOException e) {
			AudioProjectUtils.error("Error writing audio file", thread);
			return;
		}
	}
	
	private void writeBytes(){
		try {
			PrintWriter writer = new PrintWriter(getPath() + fileName + ".txt", "UTF-8");
			writer.println("Index\tValue");
			for(int i = 0; i < AudioRecordingWindow.data.length; i++){
				writer.println(i + "\t" + AudioRecordingWindow.data[i]);
			}
			
			writer.close();
		} catch (IOException e) {
			AudioProjectUtils.error("Error writing byte file", thread);
			return;
		}
	}
	
	private String appendIfExists(String f, String ext){
		File test = new File(getPath() + f + ext);
		if(test.exists()){
			int append = 0;
			while(test.exists()){
				test = new File(getPath() + f + append + ext);
				++append;
			}
			return f + (append - 1);
		}
		return f;
	}
	
	private String getPath() {
		if (!new File(System.getProperty("user.dir") + File.separator + "Audio"	+ File.separator).exists()) {
			new File(System.getProperty("user.dir") + File.separator + "Audio" + File.separator).mkdir();
		}
		return System.getProperty("user.dir") + File.separator + "Audio" + File.separator;
	}

}
