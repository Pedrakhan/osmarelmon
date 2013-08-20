package util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Converts all 'ä','ö','ü' to 'ae','ou','ue' and 'ß' to 'ss'.
 * 
 * @author sebastian
 * 
 */
public class UmlautConverter {

	private UmlautConverter() {
	}

	/**
	 * Converts all umlauts in the String to HTML code.
	 * 
	 * @param text
	 *            The String with umlauts.
	 * @return The same string without umlauts.
	 */
	public static String convertUmlautsToHTML(String text) {
		text = text.replaceAll("ä", "&auml;");
		text = text.replaceAll("ü", "&uuml;");
		text = text.replaceAll("ö", "&ouml;");
		text = text.replaceAll("Ä", "&Auml;");
		text = text.replaceAll("Ü", "&Uuml;");
		text = text.replaceAll("Ö", "&Ouml;");
		text = text.replaceAll("ß", "&szlig;");
		return text;
	}

	/**
	 * Replaces all umlauts of a String. E.g.: ä -> ae
	 * 
	 * @param text
	 *            The String with umlauts.
	 * @return The same String without umlauts.
	 */
	public static String deleteUmlauts(String text) {
		text = text.replaceAll("ä", "ae");
		text = text.replaceAll("ü", "ue");
		text = text.replaceAll("ö", "oe");
		text = text.replaceAll("Ä", "Ae");
		text = text.replaceAll("Ü", "Oe");
		text = text.replaceAll("Ö", "Ue");
		text = text.replaceAll("ß", "ss");
		return text;
	}

	/**
	 * Converts the text so it can be used in URL's.
	 * 
	 * @param text
	 * @return
	 */
	public static String makeProperURL(String text) {
		try {
			return URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
