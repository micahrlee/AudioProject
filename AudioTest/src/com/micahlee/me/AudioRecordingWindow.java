package com.micahlee.me;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioFileFormat;
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
	
	private String path;
	private AudioRecorder ar;
	private static File file;
	
	private JPanel contentPane;
	private JButton record;
	private JButton stopRecord;
	private JSpinner fileExtenstion;
	private JButton chooseFile;
	private JSeparator separator;
	private static JButton play;
	private static JTextField fileToPlay;
	private JMenuBar menuBar;
	private JMenu mnMenu;
	private JMenuItem mntmAbout;
	private JMenuItem mntmJelp;
	private JButton btnNewButton;
	private JSeparator separator_1;

	public AudioRecordingWindow() {
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
		setBounds(100, 100, 289, 164);
		
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
		
		record = new JButton("Record");
		
		stopRecord = new JButton("Stop Recording");
		stopRecord.setEnabled(false);

		fileExtenstion = new JSpinner();
		fileExtenstion.setModel(new SpinnerListModel(new String[] {"AIFC", "AIFF", "AU", "SND", "WAV"}));
		fileExtenstion.setValue("WAV");
		
		setLocationRelativeTo(null);
		
		chooseFile = new JButton("Choose File");
		
		separator = new JSeparator();
		
		play = new JButton("Play");
		play.setEnabled(false);
		
		fileToPlay = new JTextField("Choose File...");
		fileToPlay.setEditable(false);
		fileToPlay.setColumns(10);
		
		btnNewButton = new JButton("Split Phonemes");
		
		separator_1 = new JSeparator();
		
		File testFile = new File(System.getProperty("user.home") + File.separator + "Audio" + File.separator);
		if(!testFile.exists()){
			testFile.mkdir();
		}
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(fileExtenstion, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(record, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(stopRecord, GroupLayout.PREFERRED_SIZE, 102, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(fileToPlay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chooseFile)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(play, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(separator, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
								.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(fileExtenstion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(record)
						.addComponent(stopRecord))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(fileToPlay, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chooseFile)
						.addComponent(play))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(btnNewButton))
		);
		contentPane.setLayout(gl_contentPane);
		activateListeners();
	}
	
	private void activateListeners(){
		record.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AudioFileFormat.Type type = null;
				switch((String)fileExtenstion.getValue()){
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
				path = getPath();
				ar = new AudioRecorder(path, type);
				record.setEnabled(false);
				stopRecord.setEnabled(true);
				ar.start();
			}
		});
		
		stopRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				record.setEnabled(true);
				stopRecord.setEnabled(false);
				ar.stopRecord();
			}
		});
		
		chooseFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showFileChooser();
			}
		});
		
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				new Thread(new Playback(file)).start();
			}
		});
	}
	
	//File and path methods
	public static void setFile(File f){
		file = f;
		fileToPlay.setText(file.getName());
		play.setEnabled(true);
	}	
	
	private String getPath(){
		if(!new File(System.getProperty("user.dir") + File.separator + "Audio" + File.separator).exists()){
			new File(System.getProperty("user.dir") + File.separator + "Audio" + File.separator).mkdir();
		}
		return System.getProperty("user.dir") + File.separator + "Audio" + File.separator;
	}
	
	//Play methods
	private void showFileChooser(){
		JFileChooser chooser = new JFileChooser(getPath());
		chooser.addChoosableFileFilter(new AudioFilter());
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Choose your audio file");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){	
			file = chooser.getSelectedFile();
			fileToPlay.setText(file.getName());
			play.setEnabled(true);
		}
		else{
			file = null;
			fileToPlay.setText("Choose File...");
			play.setEnabled(false);
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
