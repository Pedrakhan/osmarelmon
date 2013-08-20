package model.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * Shows the error page.
 * 
 */
public class ErrorAction extends Action {

	// Konstanten fuer die von der Action gesetzen Request-Attribute
	public static final String ATTR_ERRORMSG = "errorMsg";
	public static final String ATTR_CAUSE = "cause";

	// Private Datenhaltungs-Variablen fuer die Message und die Ursache
	// des Fehlers (werden beim Konstruktor gesetzt und in execute() verwendet
	private String message;
	private Exception cause;

	/**
	 * Erstellt eine neue ErrorAction
	 * 
	 * @param message
	 *            Text der Fehlermeldung der beim Ausfuehren der Action
	 *            angezeigt werden soll
	 * @param cause
	 *            Ursache der Fehlermeldung oder null, falls es keine
	 *            entsprechende Exception gab. Ist eine Ursache gesetzt, kann
	 *            man diese auf der Fehlerseite z.B. mit Stacktrace ausgeben
	 *            lassen (siehe error.jsp)
	 */
	public ErrorAction(String message, Exception cause) {
		this.message = message
				+ " <a href=\"javascript:history.back();\">Back</a>";
		this.cause = cause;
	}

	@Override
	public String execute(HttpServletRequest request) {
		// Wir setzen einfach nur die beiden Werte als Attribute in den
		// Request, damit Fehlertext und Ursache auf der JSP-Seite zur
		// Verfuegung stehen.
		request.setAttribute(ATTR_ERRORMSG, message);
		request.setAttribute(ATTR_CAUSE, cause);
		// Die Error-JSP soll vom Controller dann aufgerufen werden.
		return util.Constants.JSP_ERROR;
	}

}
