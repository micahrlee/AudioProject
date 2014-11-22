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

public class AudioRecordingWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static AudioInputStream audioInputStream;
	private File file;
	private Recorder recorder;
	private Playback playback;
	private FileSaver fs;
	
	private JPanel contentPane;
	private JButton startRecord;
	private JButton stopRecord;
	private JButton chooseFile;
	private JButton saveAudio;
	private JButton splitPhonemes;
	public static JButton startPlay;
	
	private JSpinner fileExtension;

	private JTextField fileToPlay;
	
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mntmAbout;
	private JMenuItem mntmJelp;
	
	private JSeparator separator;
	private JSeparator separator_1;

	public AudioRecordingWindow() {
		audioInputStream = null;
		recorder = new Recorder();
		playback = new Playback();
		fs = new FileSaver();
		File testFile = new File(System.getProperty("user.home") + File.separator + "Audio" + File.separator);
		if(!testFile.exists()){
			testFile.mkdir();
		}
		setTitle("Phoneme Splitter");
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 379, 164);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		mntmJelp = new JMenuItem("Help");
		mnMenu.add(mntmJelp);
		
		mntmAbout = new JMenuItem("About");
		mnMenu.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		startRecord = new JButton("Record");
		
		stopRecord = new JButton("Stop Recording");
		stopRecord.setEnabled(false);
		
		startPlay = new JButton("Play");
		startPlay.setEnabled(false);
		
		chooseFile = new JButton("Choose File");
		
		splitPhonemes = new JButton("Split Phonemes");

		saveAudio = new JButton("Save Audio");
		saveAudio.setEnabled(false);		

		fileExtension = new JSpinner();
		fileExtension.setEnabled(false);
		fileExtension.setModel(new SpinnerListModel(new String[] {"AIFC", "AIFF", "AU", "SND", "WAV"}));
		fileExtension.setValue("WAV");
		
		setLocationRelativeTo(null);
		
		separator = new JSeparator();
		separator_1 = new JSeparator();		
		
		fileToPlay = new JTextField("Choose File...");
		fileToPlay.setEditable(false);
		fileToPlay.setColumns(10);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(startRecord, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(stopRecord, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(fileExtension, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(saveAudio, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
					.addGap(4))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(54)
					.addComponent(fileToPlay, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chooseFile, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(startPlay, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
					.addGap(44))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(9)
					.addComponent(splitPhonemes, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
					.addGap(4))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
					.addGap(4))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(fileExtension, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(saveAudio, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(stopRecord, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(startRecord, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(fileToPlay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chooseFile)
						.addComponent(startPlay))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(splitPhonemes)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		activateListeners();
	}
	
	private void activateListeners(){
		startRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				recorder.start();
				startRecord.setEnabled(false);
				stopRecord.setEnabled(true);
			}
		});
		
		stopRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				recorder.stop();
				startRecord.setEnabled(true);
				stopRecord.setEnabled(false);
				startPlay.setEnabled(true);
				saveAudio.setEnabled(true);
				fileExtension.setEnabled(true);
				fileToPlay.setText("Recorded Audio");
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
		
		splitPhonemes.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub	
			}
		});
	}
	
	//Play methods
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + File.separator + "Audio" + File.separator);
		chooser.setFileFilter(new AudioFilter());
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Choose your audio file");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){	
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
	
	//Main
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
	
	//Private class for filtering extensions
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
