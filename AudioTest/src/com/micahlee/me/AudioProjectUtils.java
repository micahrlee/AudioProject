package com.micahlee.me;

import javax.swing.JOptionPane;

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
}
