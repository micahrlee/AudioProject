package com.micahlee.me;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.Component;

/**
 * Made by Micah Lee for University of Washington CSS457. 
 * @author Micah Lee - micahrlee@gmail.com
 *
 */
public class AudioRecordingWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static AudioInputStream audioInputStream;
	public static byte[] data;
	private static File file;
	private Recorder recorder;
	private Playback playback;
	private FFTPlotter fft;
	private FileSaver fs;
	
	private JPanel contentPane;
	private JButton startRecord;
	private JButton chooseFile;
	private JButton saveAudio;
	private JButton graph;
	private JButton rawSignal;
	public static JButton startPlay;
	
	private JSpinner fileExtension;

	private JTextField fileToPlay;
	
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem about;
	
	private JSeparator separator;

	public AudioRecordingWindow() {
		audioInputStream = null;
		recorder = new Recorder();
		playback = new Playback();
		fft = new FFTPlotter();
		fs = new FileSaver();
		File testFile = new File(System.getProperty("user.home") + File.separator + "Audio" + File.separator);
		if(!testFile.exists()){
			testFile.mkdir();
		}
		setTitle("Audio Tool");
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
		setBounds(100, 100, 283, 197);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		about = new JMenuItem("About");
		mnMenu.add(about);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		startRecord = new JButton("Record");
		
		startPlay = new JButton("Play");
		startPlay.setEnabled(false);
		
		chooseFile = new JButton("Choose File");

		saveAudio = new JButton("Save Audio");
		saveAudio.setEnabled(false);		

		fileExtension = new JSpinner();
		fileExtension.setEnabled(false);
		fileExtension.setModel(new SpinnerListModel(new String[] {"AIFC", "AIFF", "AU", "SND", "WAV"}));
		fileExtension.setValue("WAV");
		
		setLocationRelativeTo(null);
		
		separator = new JSeparator();
		
		fileToPlay = new JTextField("Choose File...");
		fileToPlay.setEditable(false);
		fileToPlay.setColumns(10);
		
		JButton splitPhonemes = new JButton("Split Phonemes");
		splitPhonemes.setEnabled(false);
		
		JSeparator separator_1 = new JSeparator();
		
		graph = new JButton("Plot FFT");
		
		JSeparator separator_2 = new JSeparator();
		
		rawSignal = new JButton("Plot Signal");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(startRecord, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fileExtension, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(saveAudio, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(fileToPlay, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chooseFile, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(startPlay, GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
					.addGap(10))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(rawSignal, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(graph, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(splitPhonemes, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(startRecord, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(fileExtension, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(saveAudio, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(fileToPlay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chooseFile, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(startPlay))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(graph)
						.addComponent(rawSignal))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPhonemes)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{separator, startRecord, fileExtension, saveAudio, fileToPlay, chooseFile, startPlay}));
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuBar, mnMenu, contentPane, about, fileToPlay, chooseFile, startPlay, separator, startRecord, fileExtension, saveAudio}));
		activateListeners();
	}
	
	private void activateListeners(){
		startRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(startRecord.getText().startsWith("Record")){
					recorder.start();
					startRecord.setText("Stop");
				}
				else if(startRecord.getText().startsWith("Stop")){
					recorder.stop();
					startRecord.setText("Record");
					startPlay.setEnabled(true);
					saveAudio.setEnabled(true);
					fileExtension.setEnabled(true);
					fileToPlay.setText("Recorded Audio");
				}
			}
		});
		
		saveAudio.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AudioFileFormat.Type type = null;
				switch((String)fileExtension.getValue()){
					case "WAV":
						type = AudioFileFormat.Type.WAVE;
						break;
					case "SND":
						type = AudioFileFormat.Type.SND;
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
				}
				fs.start(type);
			}
		});
		
		chooseFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}
		});
		
		startPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if(startPlay.getText().startsWith("Play")){
					if(file == null){
						playback.start();
					}
					else{
						playback.start(file);
					}
					startPlay.setText("Stop");
				}
				else if(startPlay.getText().startsWith("Stop")){
					playback.stop();
					startPlay.setText("Play");
				}
			}
		});
		
		graph.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(file == null){
					if(data != null){
						fft.start(AudioProjectUtils.bytesToWav(data), true);
					}
					else{
						AudioProjectUtils.error("No audio to plot!", null);
					}
				}
				else{
					fft.start(file, true);
				}
			}
		});
		
		rawSignal.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(file == null){
					if(data != null){
						fft.start(AudioProjectUtils.bytesToWav(data), false);
					}
					else{
						AudioProjectUtils.error("No audio to plot!", null);
					}
				}
				else{
					fft.start(file, false);
				}
			}
		});
		
		about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					AboutWindow dialog = new AboutWindow();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "Audio" + File.separator);
		chooser.setFileFilter(new AudioFilter());
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Choose your audio file");
		if(chooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION){	
			file = chooser.getSelectedFile();
			fileToPlay.setText(file.getName());
			startPlay.setEnabled(true);
			saveAudio.setEnabled(false);
		}
		else{
			file = null;
			if(!fileToPlay.getText().startsWith("Recorded Audio")){
				fileToPlay.setText("Choose File...");
				startPlay.setEnabled(false);
			}
		}
	}
	
	public static void setFile(File f){
		file = f;
	}
	
	public static void main(String[] args){
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
	
	private class AudioFilter extends FileFilter{
		@Override
		public boolean accept(File f) {
			if(f.isDirectory()){
				return true;
			}
			Pattern p = Pattern.compile("^.*\\.(wav|WAV|aiff|AIFF|aifc|AIFC|au|AU|snd|SND)$");
			return p.matcher(f.getName()).matches();
		}

		@Override
		public String getDescription() {
			return "Audio Files";
		}
	}
}
