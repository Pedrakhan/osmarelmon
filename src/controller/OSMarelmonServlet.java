package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.actions.Action;
import model.actions.AddRelationAction;
import model.actions.ErrorAction;
import model.actions.HelpAction;
import model.actions.ResultAction;
import model.actions.SchedulingAction;
import model.actions.StartAction;

/**
 * Controller Servlet for OSMarelmon.
 * 
 * @author sebastian
 * 
 */
public class OSMarelmonServlet extends HttpServlet {

	private static final long serialVersionUID = -4618960947315402726L;

	public OSMarelmonServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// Diese Methode nimmt einen GET-Request entgegen.
		// Einfach an handleRequest weiterverweisen
		handleRequest(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		// Diese Methode nimmt einen POST-Request entgegen.
		// Einfach an handleRequest weiterverweisen
		handleRequest(req, res);
	}

	/**
	 * Diese Methode verarbeitet die HTTP-Requests. Basierend auf der im Request
	 * enthaltenen Action wird die entsprechende Action ausgefuehrt, welche die
	 * eigentliche Bearbeitung uebernimmt. Anschliessend wird an die passende
	 * JSP-Seite des Views weitergeleitet.
	 * 
	 * @param request
	 *            Der HTTP-Request
	 * @param response
	 *            Die zugehoerige HTTP-Response
	 * @throws ServletException
	 *             Falls ein Servlet-Fehler auftritt
	 * @throws IOException
	 *             Falls ein Ein/Ausgabe-Fehler auftritt
	 */
	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Action action = null;
		String nextSite = null;
		String actionID = getActionId(request, util.Constants.ACTIONID_START);
		if (actionID.equals(util.Constants.ACTIONID_START)) {
			action = new StartAction();
		} else if (actionID.equals(util.Constants.ACTIONID_ADD)) {
			action = new AddRelationAction();
		} else if (actionID.equals(util.Constants.ACTIONID_SCHE)) {
			action = new SchedulingAction();
		} else if (actionID.equals(util.Constants.ACTIONID_ERR)) {
			action = new ErrorAction("Maybe the result set was empty.", null);
		} else if (actionID.equals(util.Constants.ACTIONID_RES)) {
			action = new ResultAction();
		} else if (actionID.equals(util.Constants.ACTIONID_HELP)) {
			action = new HelpAction();
		} else {
			action = new ErrorAction("The requested action does not exist!",
					null);
		}
		nextSite = action.execute(request);
		request.getRequestDispatcher(nextSite).forward(request, response);
	}

	/**
	 * Hilfsmethode zum Auslesen des Action-Parameters. Diese Methode sucht aus
	 * dem uebergebenen HTTP-Request den Wert des Action-Parameters (=actionID),
	 * falls ein gueltiger vorhanden ist. Ist kein gueltiger Action-Parameter im
	 * Request verfuegbar wird ein festgelegter Default-Wert zurueck gegeben
	 * 
	 * @param request
	 *            Der HTTP-Request
	 * @param defaultValue
	 *            Der Default-Wert
	 * @return Die Action-ID, die im Request enthalten war
	 */
	private String getActionId(HttpServletRequest request, String defaultValue) {
		String actionStr = request.getParameter(util.Constants.PARAM_ACTIONID);
		if (actionStr == null) {
			actionStr = defaultValue;
		}
		return actionStr;
	}
}
