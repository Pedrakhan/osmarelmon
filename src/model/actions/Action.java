package model.actions;

import javax.servlet.http.HttpServletRequest;

/**
 * Diese abstrakte Klasse dient als Basis fuer alle Actions, die im Controller
 * verwendet werden.
 * 
 */
public abstract class Action {

	/**
	 * Fuehrt die Aktion aus und gibt den Namen der JSP-Seite im View zurueck,
	 * die dann vom Controller aufgerufen werden soll.
	 * 
	 * @param request
	 *            Der HTTP-Request des Aufrufs
	 * @return Name der JSP-Seite, welche nach Ausfuehrung der Action angezeigt
	 *         werden soll
	 */
	public abstract String execute(HttpServletRequest request);

	/**
	 * Hilfsmethode zum Auslesen von String-Parametern aus dem Request Gibt
	 * einen Request-Parameter als String zurueck. Wenn der Parameter im Request
	 * nicht enthalten war, wird ein Default-Wert zurueckgegeben.
	 * 
	 * @param name
	 *            Der Name des Request-Parameters
	 * @param defaultValue
	 *            Der Default-Wert fuer den Fehler-Fall
	 * @param request
	 *            Der HTTP-Request
	 * @return Der Wert des Parameters
	 */
	protected String getParameterAsString(String name, String defaultValue,
			HttpServletRequest request) {
		String value = request.getParameter(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

}
