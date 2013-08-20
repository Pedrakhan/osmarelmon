package userinterface;

/**
 * This class starts the OSMarelmon.
 * 
 * @author sebastian
 * 
 */
public class OSMarelmon {
	/**
	 * Default constructor.
	 */
	private OSMarelmon() {
	}

	/**
	 * Main method to run the GUI.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			private View osmarelmon = new View("OSMarelmon v0.45");

			/**
			 * Starts the GUI Thread.
			 */
			@Override
			public void run() {
				osmarelmon.createAndShowGUI();
			}
		});
	}

}
