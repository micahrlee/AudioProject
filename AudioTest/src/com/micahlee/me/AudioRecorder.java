package com.micahlee.me;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class AudioRecorder extends Thread{
	public static final AudioFormat format = new AudioFormat(8000.0f, 8, 1, true, false);
	public AudioFileFormat.Type type;
	public static final int bufSize = 4096;
	private TargetDataLine dataLine;
	private byte[] data;
	private final String path;
	private static String file;
	private boolean stop = true;
	
	public AudioRecorder(final String path, final AudioFileFormat.Type type){
		initDataLine();
		stop = true;
		if(type != null){
			this.type = type;
		}
		else{
			this.type = AudioFileFormat.Type.WAVE;
		}
		this.path = path;
		try {
			FileNameWindow dialog = new FileNameWindow();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModal(true);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ifExistsAppend(type);
		file += "." + type.getExtension();
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
		saveFile();
	}
	
	public void stopRecord(){
		stop = true;
		dataLine.drain();
		dataLine.stop();
		dataLine.flush();
		dataLine.close();
	}
	
	private void saveFile(){
		InputStream audioData = new ByteArrayInputStream(data);
		AudioInputStream ais = new AudioInputStream(audioData, format, (long)data.length);
		@SuppressWarnings("static-access")
		File file = new File(path + this.file);
		try {
			AudioSystem.write(ais, type, file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void ifExistsAppend(AudioFileFormat.Type type){
		File testFile = new File(path);
		ArrayList<File> list = new ArrayList<File>(Arrays.asList(testFile.listFiles()));
		int append = 0;
		if(!list.contains(file + "." + type.getExtension())){
			return;
		}
		while(contains(list, Pattern.compile(file + append + "\\..*"))){
			++append;
		}
		file += append;
	}
	
	private boolean contains(ArrayList<File> al, Pattern p){
		for(File f : al){
			if(p.matcher(f.getName()).matches()){
				return true;
			}
		}
		return false;
	}
	
	public static void setFileName(String s){
		file = s;
	}

	@Override
	public void run() {
		listen();
		JOptionPane.showMessageDialog(null, "Recording of " + file +  " has finished!", "Done", JOptionPane.INFORMATION_MESSAGE);
		AudioRecordingWindow.setFile(new File(path + file));
	}
}
