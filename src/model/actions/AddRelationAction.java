package model.actions;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import types.threads.AddThread;
import util.FileManager;
import util.UmlautConverter;

/**
 * Triggers the adding of a relation.
 */
public class AddRelationAction extends Action {

	private static final Log log = LogFactory.getLog(AddRelationAction.class);
	private static final int MAX_NAME_LENGTH = 32;

	@Override
	public String execute(HttpServletRequest request) {
		FileManager man = FileManager.getInstance();
		List<String> monRels = man.getMonitoredRelations();
		String name = getParameterAsString(util.Constants.PARAM_QUERY_NAME,
				"-1", request);

		name = name.trim();
		name = UmlautConverter.deleteUmlauts(name);

		List<String> contentList = new LinkedList<String>();
		for (int i = 1; i <= 4; i++) {
			String key = getParameterAsString(
					util.Constants.PARAM_KEY + "" + i, "-1", request);
			String value = getParameterAsString(util.Constants.PARAM_VALUE + ""
					+ i, "-1", request);
			if (isValidEntry(key)) {
				contentList.add(key);
			}
			if (isValidEntry(value)) {
				contentList.add(value);
			}
		}

		boolean isValidName = name.matches("([a-zA-Z]+ +)*[a-zA-Z]+")
				&& (name.length() <= MAX_NAME_LENGTH);

		if (isValidName && !monRels.contains(name)) {
			if ((contentList.size() % 2 == 0) && !contentList.isEmpty()) {
				AddThread tmp = new AddThread(contentList, name);
				tmp.start();
			} else {
				log.info("Invalid key-value pairs entered: "
						+ contentList.toString());
				ErrorAction erAction = new ErrorAction(
						"You entered invalid key-value pairs", null);
				return erAction.execute(request);
			}
		} else {
			log.info("Invalid name entered: \"" + name + "\"");
			ErrorAction erAction = new ErrorAction(
					"Either the name for your relation is already in use or you entered"
							+ " an invalid name. Be sure that your name consists"
							+ " only letters (at least one) and whitespaces. "
							+ "The name should be shorter then 32 characters.",
					null);
			return erAction.execute(request);
		}
		HttpSession session = request.getSession();
		request.setAttribute(StartAction.ATTR_MONRELS, monRels);
		session.setAttribute(util.Constants.PARAM_QUERY_NAME, name);
		return util.Constants.JSP_SCHEDULED;
	}

	private boolean isValidEntry(String content) {
		content = content.trim();
		if (!content.equals(util.Constants.PARAM_KEY)
				&& !content.equals(util.Constants.PARAM_VALUE)
				&& !content.equals("")) {
			return true;
		} else {
			return false;
		}
	}
}
