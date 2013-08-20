package model.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import util.FileManager;

/**
 * Shows the start page of OSMarelmon.
 * 
 */
public class StartAction extends Action {

	public static final String ATTR_MONRELS = "monRels";

	@Override
	public String execute(HttpServletRequest request) {
		FileManager man = FileManager.getInstance();
		List<String> monRels = man.getMonitoredRelations();
		request.setAttribute(ATTR_MONRELS, monRels);
		return util.Constants.JSP_START;

	}

}
