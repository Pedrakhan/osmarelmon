package controller;

import java.util.HashMap;

import types.ThreadStates;

/**
 * This class monitors all current threads and their states.
 * 
 * @author sebastian
 * 
 */
public class ThreadMonitor {

	private static HashMap<String, ThreadStates> threadResponses;

	private ThreadMonitor() {
	}

	static {
		threadResponses = new HashMap<String, ThreadStates>();
	}

	/**
	 * AddThreads must register with the name of the relation.
	 * 
	 * @param name
	 *            The unique name to identify the thread. Best would be the name
	 *            of the relation.
	 */
	public static void register(String name) {
		threadResponses.put(name, ThreadStates.STARTED);
	}

	/**
	 * Changes the state of a thread.
	 * 
	 * @param name
	 *            The name of the thread.
	 * @param state
	 *            The new state.
	 */
	public static void setState(String name, ThreadStates state) {
		ThreadStates tmp = threadResponses.remove(name);
		if (tmp != null) {
			threadResponses.put(name, state);
		}
	}

	/**
	 * 
	 * @param name
	 *            The thread's name
	 * @return the state of the thread.
	 */
	public static ThreadStates getThreadState(String name) {
		ThreadStates result = threadResponses.get(name);
		if (result == ThreadStates.STARTED) {
			return result;
		} else {
			return threadResponses.remove(name);
		}
	}
}
