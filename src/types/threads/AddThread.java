package types.threads;

import java.util.List;

import types.OverpassQuery;
import types.ThreadStates;
import util.FileManager;
import controller.ThreadMonitor;

/**
 * Thread to add a new relation to the monitor. Can be started with an Overpass
 * query or the key value pairs.
 * 
 * @author sebastian
 * 
 */
public class AddThread extends Thread {

	private List<String> tags;
	private String name;
	private OverpassQuery query;

	public AddThread(List<String> tags, String name) {
		super();
		this.tags = tags;
		this.name = name;

	}

	public AddThread(OverpassQuery query, String name) {
		super();
		this.query = query;
		this.name = name;
	}

	@Override
	public void run() {
		boolean success = false;
		ThreadMonitor.register(name);
		FileManager man = FileManager.getInstance();
		if (tags != null) {
			success = man.addNewRelationToMonitor(tags, name);
		} else if (query != null) {
			success = man.addNewRelationToMonitor(query, name);
		}

		if (success) {
			ThreadMonitor.setState(name, ThreadStates.SUCCEEDED);
		} else {
			ThreadMonitor.setState(name, ThreadStates.FAILED);
		}
	}
}