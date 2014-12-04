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
	
	public static byte[] applyFFT(byte[] signal){
		int bigN = signal.length - 1;
		byte[] retVal = new byte[signal.length];
		
		for(int k = 0; k < signal.length; k++){
			for(int n = 0; n < signal.length; n++){
				
			}
		}
		
		return retVal;
	}
}
