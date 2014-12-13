package com.micahlee.me;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * Made by Micah Lee for University of Washington CSS457. 
 * @author Micah Lee - micahrlee@gmail.com
 *
 */
public class FileNameWindow extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField fileName;

	public FileNameWindow() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Input File Name");
		setBounds(100, 100, 277, 126);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		fileName = new JTextField();
		fileName.setColumns(10);
		fileName.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent k){
				char c = k.getKeyChar();
				Pattern p = Pattern.compile("(<|>|:|\"|/|\\|\\?|\\*|\\\\|)");
				if(p.matcher(Character.toString(c).trim()).matches()){
					k.consume();
				}
			}
		});
		fileName.setToolTipText("File name must not contain <, >, :, \", |, /, \\, ?, or *");
		
		JLabel lblEnterYourFile = new JLabel("Enter your file name:");
		JButton ok = new JButton("OK");
		setLocationRelativeTo(null);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGap(107)
					.addComponent(ok, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(97))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEnterYourFile)
						.addComponent(fileName, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(7)
					.addComponent(lblEnterYourFile)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(fileName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(ok)
					.addContainerGap())
		);
		ok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!fileName.getText().trim().equals("")){
					FileSaver.fileName = fileName.getText().trim();
					dispose();
				}
			}
		});
		
		fileName.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!fileName.getText().trim().equals("")){
					FileSaver.fileName = fileName.getText().trim();
					dispose();
				}
			}
		});
		contentPanel.setLayout(gl_contentPanel);
	}
}
