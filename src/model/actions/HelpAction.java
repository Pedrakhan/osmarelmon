package model.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * Shows the help page.
 * 
 * @author sebastian
 * 
 */
public class HelpAction extends Action {

	@Override
	public String execute(HttpServletRequest request) {
		return util.Constants.JSP_HELP;
	}

}
