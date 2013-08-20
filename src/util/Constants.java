package util;

public class Constants {

	// Konstanten für die verwendeten JSP-Seiten
	public static final String JSP_START = "start.jsp";
	public static final String JSP_RESULT = "result.jsp";
	public static final String JSP_SCHEDULED = "scheduled.jsp";
	public static final String JSP_ERROR = "error.jsp";
	public static final String JSP_HELP = "help.jsp";

	// Konstanten für die verwendeten Actions
	public static final String PARAM_ACTIONID = "action";
	public static final String ACTIONID_ADD = "add";
	public static final String ACTIONID_SCHE = "scheduled";
	public static final String ACTIONID_ERR = "error";
	public static final String ACTIONID_RES = "result";
	public static final String ACTIONID_START = "start";
	public static final String ACTIONID_HELP = "help";

	// Konstanten für Requestparameter
	public static final String PARAM_QUERY_NAME = "queryName";
	public static final String PARAM_KEY = "key";
	public static final String PARAM_VALUE = "value";

	// FileManager Konstanten
	public static final String OSMARELMON_STANDARD_FEED = "OSMarelmon";
	public static final String DEBUG_FEED = "Debug Feed";
	public static final String FILE_ENDING = ".myosm";

	// ubuntu tomcat
	public static final String HOME_DIR = "/var/lib/tomcat7/webapps/OSMarelmon/";

	// Konstante, wie lang ein RSS Feed leben darf ohne besucht zu werden: 1000
	// * 60 * 60 * 24 * 7 * 4 = 2419200000L
	public static final long MAX_RSS_NOT_VISITED = 2419200000L;// 4 Wochen
	public static int MAX_RSS_MESSAGES = 200;
	public static int DELETE_FIRST_X_MESSAGES = 80;

	// Dauer, die der CheckThread schläft
	public static final int REFRESH_TIME = 1000 * 60 * 60 * 12;

	private Constants() {
	}

}
