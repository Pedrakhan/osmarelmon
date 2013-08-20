package model.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * Shows the scheduling page.
 * 
 * @author sebastian
 * 
 */
public class SchedulingAction extends Action {

	@Override
	public String execute(HttpServletRequest request) {
		return util.Constants.JSP_SCHEDULED;
	}
}
