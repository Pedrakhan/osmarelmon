package util;

import javax.swing.JOptionPane;

/**
 * The PopUpManager displays PopUp messages when needed.
 * 
 * 
 * >>>>>>>>>>>>>>Unused for web application<<<<<<<<<<<<<<
 * 
 * @author sebastian
 * 
 */
public final class PopUpManager {

	private PopUpManager() {
	}

	public static int showConfirmDialogue() {
		int n = JOptionPane.showConfirmDialog(null,
				"Would you like to execute this query??", "Confirm",
				JOptionPane.YES_NO_OPTION);
		return n;
	}

	public static void showErrorDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error!",
				JOptionPane.ERROR_MESSAGE);
	}

	public static void showSuccessDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error!",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
