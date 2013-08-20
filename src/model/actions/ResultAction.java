package model.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * Shows the result page (success).
 * 
 * @author sebastian
 * 
 */
public class ResultAction extends Action {

	@Override
	public String execute(HttpServletRequest request) {
		return util.Constants.JSP_RESULT;
	}

}
