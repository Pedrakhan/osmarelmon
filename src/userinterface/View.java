package userinterface;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import types.OverpassQuery;
import types.threads.AddThread;
import types.threads.CheckThread;
import util.FileManager;
import util.PopUpManager;

/**
 * Defines the look of OSMarelmon.
 * 
 * @author sebastian
 * 
 */
public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField[] textFields;
	private JTextField nameForRelation;
	private static final int KEY_VALUE = 2;
	private static final int SHOW_TEXTFIELDS = 2;
	private static final int TEXTFIELDS = 8;
	private List<String> monRels;
	private static final String KEY_STRING = "Key";
	private static final String VALUE_STRING = "Value";
	private CheckThread checkThread;
	private JComboBox<OverpassQuery> queries;
	private FileManager man;

	/**
	 * Constructor to set the frame title.
	 * 
	 * @param text
	 *            The title of the frame.
	 */
	public View(String text) {
		super(text);
		textFields = null;
		nameForRelation = null;
		man = FileManager.getInstance();
		monRels = man.getMonitoredRelations();
	}

	/**
	 * Triggers the building of the GUI.
	 */
	public void createAndShowGUI() {
		this.setMinimumSize(new Dimension(600, 500));
		this.setPreferredSize(new Dimension(600, 500));
		// places the view in the middle of the screen
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.pack();
		buildGUI();
	}

	private void buildGUI() {

		// set up container
		Container main = this.getContentPane();
		main.setLayout(new GridLayout(3, 1));
		JPanel selfPanel = new JPanel(new GridLayout(7, 2));
		JLabel keyLabel = new JLabel(KEY_STRING);
		JLabel valueLabel = new JLabel(VALUE_STRING);
		selfPanel.add(keyLabel);
		selfPanel.add(valueLabel);
		selfPanel
				.setBorder(BorderFactory.createTitledBorder("Search by tags:"));
		textFields = new JTextField[TEXTFIELDS];
		for (int i = 0; i < textFields.length; i++) {
			if (i % KEY_VALUE == 0) {
				textFields[i] = new JTextField(KEY_STRING);
			} else {
				textFields[i] = new JTextField(VALUE_STRING);
			}
			if (i >= SHOW_TEXTFIELDS) {
				textFields[i].setVisible(false);
			}
			selfPanel.add(textFields[i]);
		}
		nameForRelation = new JTextField();
		JButton search = new JButton(addRelation());
		JButton add = new JButton(moreFields());
		selfPanel.add(new JLabel("Enter name for query:"));
		selfPanel.add(nameForRelation);
		selfPanel.add(search);
		selfPanel.add(add);

		JPanel prePanel = new JPanel(new GridLayout(2, 2));
		prePanel.setBorder(BorderFactory
				.createTitledBorder("Search by predefined queries:"));
		queries = new JComboBox<OverpassQuery>();
		getPredefinedQueries();
		queries.setBorder(BorderFactory.createTitledBorder("Select"));
		prePanel.add(queries);
		prePanel.add(new JButton(executePredefinedQuery()));

		JPanel lastPanel = new JPanel(new GridLayout(1, 1));
		JPanel rels = new JPanel(new GridLayout(monRels.size(), 1));
		JScrollPane pane = new JScrollPane(rels);

		lastPanel.setBorder(BorderFactory
				.createTitledBorder("Already monitored:"));
		if (monRels != null) {
			for (String rel : monRels) {
				JLabel tmp = new JLabel(rel);
				rels.add(tmp);
			}
		}
		lastPanel.add(pane);
		main.add(selfPanel);
		main.add(prePanel);
		main.add(lastPanel);

		checkThread = new CheckThread();
		checkThread.start();
	}

	private void getPredefinedQueries() {
		OverpassQuery highways = new OverpassQuery("network", "BAB",
				"operator", "Bundesrepublik Deutschland");
		highways.setQueryName("All German highways");
		queries.addItem(highways);
	}

	private Action executePredefinedQuery() {
		Action execute = new AbstractAction("Execute query") {

			private static final long serialVersionUID = 8369590578243246401L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Object tmp = queries.getSelectedItem();
				if (tmp instanceof OverpassQuery) {
					OverpassQuery query = (OverpassQuery) tmp;
					if (PopUpManager.showConfirmDialogue() == 0) {
						AddThread temp = new AddThread(query, query.toString());
						temp.start();
					}
				} else {
					PopUpManager
							.showErrorDialog("Failed to get the selected query");
				}
			}

		};
		return execute;
	}

	private Action addRelation() {
		Action search = new AbstractAction("Add relation") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameForRelation.getText();
				name = name.trim();
				if (!name.equals("") && !monRels.contains(name)) {
					List<String> contentList = generateContentList();
					if (PopUpManager.showConfirmDialogue() == 0
							&& (contentList.size() % 2 == 0)
							&& !contentList.isEmpty()) {
						AddThread tmp = new AddThread(contentList, name);
						tmp.start();
					} else {
						PopUpManager
								.showErrorDialog("You entered invalid key - "
										+ "value pairs.");
					}
				} else {
					PopUpManager.showErrorDialog("You must enter a name for "
							+ "your query.");
				}
			}
		};
		return search;
	}

	/**
	 * Generates a list of strings that are contained in the texfields.
	 * 
	 * @return A list of strings containing valid strings to build a query
	 */
	private List<String> generateContentList() {
		List<String> tmp = new LinkedList<String>();
		for (int i = 0; i < textFields.length; i++) {
			String content = textFields[i].getText();
			if (isValidContent(content)) {
				tmp.add(content);
			}
		}
		return tmp;
	}

	/**
	 * Checks for the default values of the textfields and if it is an empty
	 * string.
	 * 
	 * @param content
	 *            The content of the textfield.
	 * @return {@code True} if the content was not empty and doesnt match the
	 *         default values.
	 */
	private boolean isValidContent(String content) {
		content = content.trim();
		if (!content.equals(KEY_STRING) && !content.equals(VALUE_STRING)
				&& !content.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Displays more Textfields.
	 * 
	 * @return The action that displays more textfields.
	 */
	private Action moreFields() {
		Action moreFields = new AbstractAction("More fields") {

			private static final long serialVersionUID = 1L;
			private static final int SET_FIELDS_VISIBLE = 2;

			@Override
			public void actionPerformed(ActionEvent e) {
				int counter = 0;
				for (int i = 0; i < textFields.length; i++) {
					if (!textFields[i].isVisible()
							&& counter < SET_FIELDS_VISIBLE) {
						textFields[i].setVisible(true);
						counter++;
					}
				}
			}
		};
		return moreFields;
	}
}