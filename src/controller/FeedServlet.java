package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rss.RSSFeedManager;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Sample Servlet that serves a feed created with ROME.
 * <p>
 * The feed type is determined by the 'type' request parameter, if the parameter
 * is missing it defaults to the 'default.feed.type' servlet init parameter, if
 * the init parameter is missing it defaults to 'atom_0.3'
 * <p>
 * 
 * @author Alejandro Abdelnur
 * 
 */
public class FeedServlet extends HttpServlet {

	private static final long serialVersionUID = 6312305004972532985L;

	public static final String PARAM_ACTIONID = "action";
	private static final String DEFAULT_FEED_TYPE = "default.feed.type";
	private static final String FEED_TYPE = "type";
	private static final String MIME_TYPE = "application/xml; charset=UTF-8";
	private static final String COULD_NOT_GENERATE_FEED_ERROR = "Could not generate feed";

	private String _defaultFeedType;

	public void init() {
		_defaultFeedType = getServletConfig().getInitParameter(
				DEFAULT_FEED_TYPE);
		_defaultFeedType = (_defaultFeedType != null) ? _defaultFeedType
				: "rss_2.0";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String queryName = getQueryName(request, "-1");

		try {
			SyndFeed feed = RSSFeedManager.getRSSFeed(queryName);
			String feedType = request.getParameter(FEED_TYPE);
			feedType = (feedType != null) ? feedType : _defaultFeedType;
			if (feed != null) {
				feed.setFeedType(feedType);
				response.setContentType(MIME_TYPE);
				SyndFeedOutput output = new SyndFeedOutput();
				output.output(feed, response.getWriter());
			} else {
				String msg = COULD_NOT_GENERATE_FEED_ERROR;
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,
						msg);
			}
		} catch (FeedException ex) {
			String msg = COULD_NOT_GENERATE_FEED_ERROR;
			log(msg, ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					msg);
		}
	}

	private String getQueryName(HttpServletRequest request, String defaultValue) {
		String actionStr = request.getParameter(PARAM_ACTIONID);
		if (actionStr == null) {
			actionStr = defaultValue;
		}
		return actionStr;
	}

}